package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.*;
import com.example.VivaLaTrip.Form.*;
import com.example.VivaLaTrip.Repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Slf4j
@Service
@RequiredArgsConstructor
public class PlanService {
    private final PublicPlanRepository publicPlanRepository;
    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanDetailRepository planDetailRepository;
    private final MapRepository mapRepository;

    @Autowired
    PublicPlanService publicPlanService;
    @Autowired
    MapService mapService;
    public void savePlan(PlanSaveDTO request, User user, List<PlaceComputeDTO> computeDTO) {

        //코멘트 값 입력받지 않았다면 No title 저장
        if (request.getTitle() == null) {
            request.setTitle("No title");
        }

        Plan plan = new Plan();
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());

        plan.setUserInfo(userInfo.get());
        plan.setIs_public(false);
        plan.setTotal_count(request.getCheckedPlace().size());
        plan.setStart_date(request.getStart_date());
        plan.setEnd_date(request.getEnd_date());
        plan.setComment(request.getTitle());        //타이틀 추가
        //plan 객체에 필요한 값들 설정

        planRepository.save(plan);

        if(request.getIsPublic()){
            log.info(plan.getPlanId().toString());
            log.info(request.getTitle());
            log.info(user.getUsername());
            publicPlanService.toPublic(plan.getPlanId());
        }

        savePlanDetail(computeDTO, user, plan);
    }

    public LoginSuccessPlanListDTO getMyPlans(User user, Pageable pageable) {

        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        List<PlanListDTO> listDTO = new ArrayList<>();
        Page<Plan> user_plan = planRepository.findByUserInfo_UserId(userInfo.get().getUserId(),pageable);
        //userinfo에서 id값 가져온거 plan리포지토리 userId에서 찾음= plan이 있는 값 가져옴, Pageable로 페이지 형식 맞춰서 뽑아옴

        log.info("전체 페이지 수 확인 : " + user_plan.getTotalPages());
        log.info("getTotalElements() 확인 : " + user_plan.getTotalElements());

        for(Plan plan: user_plan){
            if(plan.getIs_public()) {

                PublicPlan publicPlan = publicPlanRepository.findByPlanId(plan.getPlanId());

                PlanListDTO planListItem = PlanListDTO.builder()
                        .userId(plan.getUserInfo().getUserId().toString())
                        .start_date(plan.getStart_date())
                        .end_date(plan.getEnd_date())
                        .plan_id(plan.getPlanId().toString())
                        .title(plan.getComment())
                        .place_num(plan.getTotal_count())
                        .liked(publicPlan.getLike_count())
                        .build();

                listDTO.add(planListItem);
            }
            else {
                PlanListDTO planListItem = PlanListDTO.builder()
                        .userId(plan.getUserInfo().getUserId().toString())
                        .start_date(plan.getStart_date())
                        .end_date(plan.getEnd_date())
                        .plan_id(plan.getPlanId().toString())
                        .title(plan.getComment())       //isPublic이 false여도 코멘트 추가
                        .place_num(plan.getTotal_count())
                        .liked(0)
                        .build();

                listDTO.add(planListItem);
            }
        }

        LoginSuccessPlanListDTO loginSuccessPlanListDTO = new LoginSuccessPlanListDTO();
        loginSuccessPlanListDTO.setTotalPage(user_plan.getTotalPages());
        loginSuccessPlanListDTO.setLoginSuccess(true);
        loginSuccessPlanListDTO.setMyPlans(listDTO);
        return loginSuccessPlanListDTO;
    }

    public void savePlanDetail(List<PlaceComputeDTO> placeComputeDTOList, User user, Plan plan){

        int size = placeComputeDTOList.size();   //마지막 장소 저장을 위해 크기 구하기       //컴퓨트 디티오로 변경
        int dayIndex = 1;
        String placeIdsOfDay = "";    //place_id 문자열
        PlanDetail planDetail = new PlanDetail();

        for (int i=0;i<size;i++){
            if (placeComputeDTOList.get(i).getDays() != dayIndex){  //day가 올라가면
                planDetail.setPlan(plan);
                planDetail.setPlace_id(placeIdsOfDay);
                planDetail.setDays(dayIndex);
                planDetailRepository.save(planDetail);
                planDetail = new PlanDetail();
                placeIdsOfDay = "";
                dayIndex++;
            }

            //PlanDetailDTO에 담긴 places의 Id값을 검사해서 places객체 하나를 DB에서 뽑아옴
            Places places = mapRepository.findById(placeComputeDTOList.get(i).getId());
            //해당 places의 popularity에 +1을 해주고
            places.setPopularity(places.getPopularity()+1);
            //places업데이트
            mapRepository.save(places);

            placeIdsOfDay+=placeComputeDTOList.get(i).getId()+",";

            if (i+1==size){  //마지막 인덱스
                planDetail.setPlan(plan);
                planDetail.setPlace_id(placeIdsOfDay);
                planDetail.setDays(dayIndex);
                planDetailRepository.save(planDetail);
            }
        }
    }

    public PlanDetailResponseDTO getPlanDetail(Long planId,User user,String whereRequest) {

        Long before = System.currentTimeMillis();

        Plan plan = planRepository.findByPlanId(planId);
        PublicPlan publicPlan = publicPlanRepository.findByPlanId(planId);
        List<PlanDetail> planDetail = planDetailRepository.findAllByPlan_PlanId(planId);

        //일정이 자기 일정인지 확인하기 whereRequest가 true면 일반일정에서 요청 함 자기 일정만 조회 가능
        if (whereRequest.equals("plan")) {      //이중 포문인 이유는 로그인 하지 않아도 public_plan은 조회가능 안그러면 user is null에러 뜸
            if (!user.getUsername().equals(plan.getUserInfo().getUsername())) {
                throw new IllegalStateException("해당 일정은 니것이 아님");
            }
        }


        PlanDetailResponseDTO planDetailResponseDTO = new PlanDetailResponseDTO();
        List<PlanDetailDTO> places = new ArrayList<>();

        if (plan.getIs_public()){
            planDetailResponseDTO.setTitle(plan.getComment());
            planDetailResponseDTO.setLiked(publicPlan.getLike_count());
        }else {
            planDetailResponseDTO.setTitle("");
            planDetailResponseDTO.setLiked(0);
        }

        planDetailResponseDTO.setUserId(plan.getUserInfo().getUserId());
        planDetailResponseDTO.setStart_date(plan.getStart_date());
        planDetailResponseDTO.setEnd_date(plan.getEnd_date());
        planDetailResponseDTO.setPlan_id(plan.getPlanId());
        planDetailResponseDTO.setPlace_num(plan.getTotal_count());

        for (PlanDetail planDetailDay : planDetail){

            String[] placeIdList = planDetailDay.getPlace_id().split(",");
            for (String placeId : placeIdList){
                PlanDetailDTO place = new PlanDetailDTO();
                place.setId(placeId);
                place.setDay(planDetailDay.getDays());
                places.add(place);
            }
            PlanDetailDTO place = new PlanDetailDTO();  //호텔용 빈자리 , 배열 맨 마지막에도 들어감 주의@@
            place.setDay(0);
            //places.add(place);
        }
//        places.remove(places.size()-1); //마지막 호텔 지움
        for (PlanDetailDTO place : places){
            if (place.getDay() != 0){
                Places p = mapRepository.findById(place.getId());

                place.setPlace_name(p.getPlace_name());
                place.setX(p.getX());
                place.setY(p.getY());
                place.setChecked(true);
            }

        }
        double avgX = 0;
        double avgY = 0;
        for (PlanDetailDTO p : places){
            if (p.getDay() != 0){
                avgX += p.getX();
                avgY += p.getY();
            }
        }

        avgX = avgX / plan.getTotal_count();
        avgY = avgY / plan.getTotal_count();

        /*PlanDetailDTO hotel = mapService.getHotel(avgX, avgY);

        for (PlanDetailDTO place : places) {
            if (place.getDay() == 0) {  //호텔 자리
                place.setPlace_name(hotel.getPlace_name());
                place.setId(hotel.getId());
                place.setX(hotel.getX());
                place.setY(hotel.getY());
                place.setChecked(true);
            }
        }*/

        planDetailResponseDTO.setPlaces(places);

        Long after = System.currentTimeMillis();
        System.out.println("getPlanDetail : "+(after - before) +"ms 소요");

        return planDetailResponseDTO;
    }

    public void updateMyPlan(UpdatePlanDTO updatePlanDTO,Long planId,User user) {

        Plan plan = planRepository.findByPlanId(planId);
        List<PlanDetail> planDetailList = planDetailRepository.findAllByPlan_PlanId(planId);

        log.info("전달 받은 값 확인" + updatePlanDTO.toString());

        //날짜 작업
        String start_date;
        start_date = updatePlanDTO.getStart_date().substring(0, 4);
        start_date += updatePlanDTO.getStart_date().substring(5, 7);
        start_date += updatePlanDTO.getStart_date().substring(8, 10);
        String end_date;
        end_date = updatePlanDTO.getEnd_date().substring(0, 4);
        end_date += updatePlanDTO.getEnd_date().substring(5, 7);
        end_date += updatePlanDTO.getEnd_date().substring(8, 10);


        //최대 데이값 담을 변수
        int findMaxDay = 0;

        //최대 데이값 확인
        for (int i = 0; i < updatePlanDTO.getPlaces().size(); i++) {
            if (findMaxDay < updatePlanDTO.getPlaces().get(i).getDay()) {
                findMaxDay = updatePlanDTO.getPlaces().get(i).getDay();
            }
        }

        //day당 개수 담는 그릇
        int countOfDays[] = new int[findMaxDay];
        for (int i = 0; i < updatePlanDTO.getPlaces().size(); i++) {
            countOfDays[updatePlanDTO.getPlaces().get(i).getDay() - 1]++;
        }

        for (int i = 0; i < countOfDays.length; i++) {
            log.info((i+1)+"데이당 개수 확인" + countOfDays[i]);
        }

        int startDay = 1;
        int equalcountOfDays = 0;
        //day(N)의 개수만큼 순서대로 담음
        for (int i = 0; i < updatePlanDTO.getPlaces().size(); i++) {
            updatePlanDTO.getPlaces().get(i).setDay(startDay);
            equalcountOfDays++;
            if (equalcountOfDays == countOfDays[startDay-1]) {
                //데이 개수값과 일치하는순간 초기화
                equalcountOfDays = 0;
                //데이값 +1
                startDay++;
            }
        }

        log.info("수정된 데이 값 확인" + updatePlanDTO.toString());

        plan = Plan.builder()
                .planId(plan.getPlanId())
                .is_public(updatePlanDTO.getIsPublic())
                .total_count(plan.getTotal_count())
                .start_date(start_date)
                .end_date(end_date)
                .fromPlanId(plan.getFromPlanId())
                .build();
        plan.setUserInfo(userRepository.findByID(user.getUsername()).get());
        log.info(plan.toString());


        //PublicPlan이였던 것이 isPulic이 false로 PublicPlan테이블에서 삭제
        if (updatePlanDTO.getIsPublic()==false && publicPlanRepository.existsByPlanId(planId)) {
            log.info("일정 취소 실행 되는지 ?");
            publicPlanService.toPrivate(planId, user);
        } else if (updatePlanDTO.getIsPublic() && publicPlanRepository.existsByPlanId(planId) == false) {
            log.info("일정 공유 실행되는지 ?");
            publicPlanService.toPublic(planId);     //코멘트는 일반 Plan 테이블에 저장
        }

        //plan업데이트
        planRepository.save(plan);

        int dayIndex = 1;
        for (int i = 0; i < planDetailList.size(); i++) {
            String placeIdsOfDay = "";
            for (int j = 0; j < updatePlanDTO.getPlaces().size(); j++) {
                if (planDetailList.get(i).getDays() == updatePlanDTO.getPlaces().get(j).getDay()) {
                    placeIdsOfDay+=updatePlanDTO.getPlaces().get(j).getId()+",";
                }
                planDetailList.get(i).setPlace_id(placeIdsOfDay);
            }
            planDetailRepository.save(planDetailList.get(i));
        }
    }
}