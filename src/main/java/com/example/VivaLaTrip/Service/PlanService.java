package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.*;
import com.example.VivaLaTrip.Form.*;
import com.example.VivaLaTrip.Repository.*;
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

    public void savePlan(PlanSaveDTO request, User user, List<PlaceComputeDTO> computeDTO) {

        Plan plan = new Plan();
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        //로그인 한 user객체에서 userId(1,2,3,...)값 가져와서 리포지토리 아이디 찾는 메소드 호출

        plan.setUserInfo(userInfo.get());
        plan.setIs_public(false);
        plan.setTotal_count(request.getCheckedPlace().size());
        plan.setStart_date(request.getStart_date());
        plan.setEnd_date(request.getEnd_date());
        //plan 객체에 필요한 값들 설정

        planRepository.save(plan);

        if(request.getIsPublic()){
            log.info(plan.getPlanId().toString());
            log.info(request.getTitle());
            log.info(user.getUsername());
            publicPlanService.toPublic(plan.getPlanId(), request.getTitle());
        }

        savePlanDetail(computeDTO, user, plan);
    }

    public LoginSuccessPlanListDTO getMyPlans(User user, Pageable pageable) {

        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        List<PlanListDTO> listDTO = new ArrayList<>();
        Page<Plan> user_plan = planRepository.findByUserInfo_UserId(userInfo.get().getUserId(),pageable);
        //userinfo에서 id값 가져온거 plan리포지토리 userId에서 찾음= plan이 있는 값 가져옴, Pageable로 페이지 형식 맞춰서 뽑아옴

        log.info("전체 페이지 수 확인 : " + user_plan.getTotalPages());
        log.info("get().toString() 확인 : " + user_plan.get().toString());
        log.info("getTotalElements() 확인 : " + user_plan.getTotalElements());

        for(Plan plan: user_plan){
            if(plan.getIs_public()) {

                PublicPlan publicPlan = publicPlanRepository.findByPlanId(plan.getPlanId());

                PlanListDTO planListItem = PlanListDTO.builder()
                        .userId(plan.getUserInfo().getUserId().toString())
                        .start_date(plan.getStart_date())
                        .end_date(plan.getEnd_date())
                        .plan_id(plan.getPlanId().toString())
                        .title(publicPlan.getComment())
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
                        .title("")
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

            Places places = mapRepository.findById(placeComputeDTOList.get(i).getId());
            //PlanDetailDTO에 담긴 places의 Id값을 검사해서 places객체 하나를 DB에서 뽑아옴
            places.setPopularity(places.getPopularity()+1);
            //해당 places의 popularity에 +1을 해주고
            mapRepository.save(places);
            //places업데이트

            placeIdsOfDay+=placeComputeDTOList.get(i).getId()+",";

            if (i+1==size){  //마지막 인덱스
                planDetail.setPlan(plan);
                planDetail.setPlace_id(placeIdsOfDay);
                planDetail.setDays(dayIndex);
                planDetailRepository.save(planDetail);
            }
        }
    }

    public PlanDetailResponseDTO getPlanDetail(Long planId){

        Long before = System.currentTimeMillis();

        Plan plan = planRepository.findByPlanId(planId);
        PublicPlan publicPlan = publicPlanRepository.findByPlanId(planId);
        List<PlanDetail> planDetail = planDetailRepository.findAllByPlan_PlanId(planId);

        PlanDetailResponseDTO planDetailResponseDTO = new PlanDetailResponseDTO();
        List<PlanDetailDTO> places = new ArrayList<>();

        if (plan.getIs_public()){
            planDetailResponseDTO.setTitle(publicPlan.getComment());
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
            for (String aaa : placeIdList){
                PlanDetailDTO place = new PlanDetailDTO();
                place.setId(aaa);
                place.setDay(planDetailDay.getDays());
                places.add(place);
            }
        }

        for (PlanDetailDTO place : places){
            Places p = mapRepository.findById(place.getId());

            place.setPlace_name(p.getPlace_name());
            place.setX(p.getX());
            place.setY(p.getY());
            place.setChecked(true);
        }

        planDetailResponseDTO.setPlaces(places);

        Long after = System.currentTimeMillis();
        System.out.println("getPlanDetail : "+(after - before) +"ms 소요");

        return planDetailResponseDTO;
    }
}