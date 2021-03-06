package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Config.RestException;
import com.example.VivaLaTrip.Entity.*;
import com.example.VivaLaTrip.Form.*;
import com.example.VivaLaTrip.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    PlanDetailService planDetailService;
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
        plan.setFromPlanId(Long.valueOf(0));
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
                throw new RestException(HttpStatus.MOVED_PERMANENTLY, "해당일정은 니 것이 아님");            }
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

        //날자 형식 작업 ex) 2020-05-31
        String start_date = plan.getStart_date();
        String end_date = plan.getEnd_date();
        start_date = start_date.substring(0, 4) + "-" + start_date.substring(4, 6) + "-" + start_date.substring(6, 8);
        end_date = end_date.substring(0, 4) + "-" + end_date.substring(4, 6) + "-" + end_date.substring(6, 8);

        planDetailResponseDTO.setUserId(plan.getUserInfo().getUserId());
        planDetailResponseDTO.setStart_date(start_date);                //날짜 형식 삽입
        planDetailResponseDTO.setEnd_date(end_date);                    //날짜 형식 삽입
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

    public void updatePlan(UpdatePlanDTO updatePlanDTO, Long planId, User user) throws ParseException {

        Plan plan = planRepository.findByPlanId(planId);
        List<PlanDetail> planDetailList = planDetailRepository.findAllByPlan_PlanId(planId);

        String start_date;
        start_date = updatePlanDTO.getStart_date().substring(0, 4);
        start_date += updatePlanDTO.getStart_date().substring(5, 7);
        start_date += updatePlanDTO.getStart_date().substring(8, 10);
        String end_date;
        end_date = updatePlanDTO.getEnd_date().substring(0, 4);
        end_date += updatePlanDTO.getEnd_date().substring(5, 7);
        end_date += updatePlanDTO.getEnd_date().substring(8, 10);

        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(start_date);
        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(end_date);
        long diffSec = (format2.getTime() - format1.getTime()) / 1000;
        long total_day = diffSec / (24 * 60 * 60) + 1;

        plan.setTotal_count(updatePlanDTO.getPlaces().size());
        plan.setStart_date(start_date);
        plan.setEnd_date(end_date);
        plan.setComment(updatePlanDTO.getTitle());
        planRepository.save(plan);

        if (!updatePlanDTO.getIsPublic() && publicPlanRepository.existsByPlanId(planId)) {
            publicPlanService.toPrivate(planId, user);
        } else if (updatePlanDTO.getIsPublic() && !publicPlanRepository.existsByPlanId(planId)) {
            publicPlanService.toPublic(planId);     //코멘트는 일반 Plan 테이블에 저장
        }

        List<PlaceComputeDTO> placeComputeDTO = new ArrayList<>();

        for (Places place : updatePlanDTO.getPlaces()) {
            PlaceComputeDTO placeItem = PlaceComputeDTO.builder()
                    .id(place.getId())
                    .x(place.getX())
                    .y(place.getY())
                    .stay(2)
                    .days(0)
                    .slope(0)
                    .where("")
                    .build();
            placeComputeDTO.add(placeItem);
        }
        double sumOfStay = 0;
        for (PlaceComputeDTO place : placeComputeDTO){
            sumOfStay += place.getStay();
        }
        double avgStayOfDays = sumOfStay / total_day;
        placeComputeDTO = planDetailService.divideDays(placeComputeDTO, avgStayOfDays);
        placeComputeDTO = planDetailService.pushDays(placeComputeDTO, updatePlanDTO.getPlaces().size(), (int) total_day, avgStayOfDays);

        planDetailRepository.deleteAll(planDetailList);

        savePlanDetail(placeComputeDTO, user, plan);
    }
}