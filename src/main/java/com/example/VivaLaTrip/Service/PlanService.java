package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.*;
import com.example.VivaLaTrip.Form.PlanDetailDTO;
import com.example.VivaLaTrip.Form.PlanListDTO;
import com.example.VivaLaTrip.Form.PlanRequestDto;
import com.example.VivaLaTrip.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@Slf4j
@RequiredArgsConstructor
@Service //스프링빈 컨테이너에 등록
public class PlanService {
    private final PublicPlanRepository publicPlanRepository;
    private final PlanRepository planRepository;
    private final PlanDetailRepository planDetailRepository;
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;
    private final PlanDetailRepository planDetailRepository;


    public void setPlan_list(List<Places> map, User user) {
        Plan plan = new Plan();
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        //로그인 한 user객체에서 userId(1,2,3,...)값 가져와서 리포지토리 아이디 찾는 메소드 호출

        //planRequestDto.toEntity().setUserInfo(planRequestDto.toEntity().getUserInfo());
        //PlanRequestDto planRequestDto = new PlanRequestDto();

        plan.setUserInfo(userInfo.get());
        plan.set_public(false);
        plan.setTotal_count(map.size());
        plan.setStart_date("20200101");
        plan.setEnd_date("20200110");
        //plan 객체에 필요한 값들 설정

        //log.info("user id" + userInfo);

        planRepository.save(plan);//메소드 이용하여 저장

        savePlanDetail(map, user, plan);
    }

    public List<PlanDetailDTO> setPlan_detail(User user, String planid) {
        PlanDetail planDetail = new PlanDetail();

        Plan plan = planRepository.findByPlanId(Long.valueOf(planid));
        //planId가 있는 plan을 찾음

        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        //로그인 한 user객체에서 userId(1,2,3,...)값 가져와서 리포지토리 아이디 찾는 메소드 호출

        List<Plan> userplan = planRepository.findAllByPlanId(userInfo.get().getUserId());
        //userinfo에서 userId값 가져와서

        List<PlanDetailDTO> detailDTO = new ArrayList<>();
        //결과를 담을 DTO list 선언(front로 보내줄 거)

        log.info("플랜아이디"+userplan.get(0).getPlanId().toString());
        log.info("플랜아이디"+planid);

        log.info("가보자궁"+plan.getPlanId().toString());

        /*planDetail.setPlan(userplan.get(0));
        planDetail.setDays();
        planDetail.setPlace_id();
        planDetail.setplan(planList.getPlanId());*/

        for(Plan a: userplan){//Plan이 있으면 반복
            /*if(a.is_public())//공유여부 참일 때
            {*/
                PlanDetailDTO planDetailItem = PlanDetailDTO.builder()//DTO객체에 저장
                        .id(a.getPlanId().toString())//planId갖고오고
                        //.place_id()//어디서 가져오지..
                        //.day()
                        .build();

                detailDTO.add(planDetailItem);

            }


       planDetailRepository.save(planDetail);

       return detailDTO;

    }



    public List<PlanListDTO> mypage_planlist(User user) {
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        //로그인 한 사람의 정보를 userInfo에 넣음

        List<PlanListDTO> listDTO = new ArrayList<>();
        //결과를 담을 DTO list 선언(front로 보내줄 거)

        List<Plan> user_plan = planRepository.findAllByUserInfo_UserId(userInfo.get().getUserId());
        //userinfo에서 id값 가져온거 plan리포지토리 userId에서 찾음= plan이 있는 값 가져옴


        for(Plan a: user_plan){//Plan이 있으면 반복
            if(a.is_public())//공유여부 참일 때
            {
                //publicPlan.add(publicPlanRepository.findByPlanId(a.getPlanId()));
                //plan이 있는 id값 가져와서 -> repository publicplan 객체에서 id 찾아와서 저장

                PlanListDTO planListItem = PlanListDTO.builder()//DTO객체에 저장
                        .userId(a.getUserInfo().getUserId().toString())
                        .start_date(a.getStart_date())
                        .end_date(a.getEnd_date())
                        .plan_id(a.getPlanId().toString())
                        .title(publicPlanRepository.findByPlanId(a.getPlanId()).getComment())
                        .place_num(a.getTotal_count())
                        .liked(publicPlanRepository.findByPlanId(a.getPlanId()).getLike_count())
                        .build();

                listDTO.add(planListItem);
            }
            else
            {//is public이 false일 때 title과 comment는 아무것도 없음
                PlanListDTO planListItem = PlanListDTO.builder()
                        .userId(a.getUserInfo().getUserId().toString())
                        .start_date(a.getStart_date())
                        .end_date(a.getEnd_date())
                        .plan_id(a.getPlanId().toString())
                        .title("")
                        .place_num(a.getTotal_count())
                        .liked(0)
                        .build();

                listDTO.add(planListItem);
            }
        }
        return listDTO;
    }

    public void savePlanDetail(List<Places> map, User user, Plan plan){
        PlanDetail planDetail = new PlanDetail();
        List<PlanDetailDTO> planDetailDTO = new ArrayList<>();  //임시 생성-나중에 매개변수로 받을거

        int size = planDetailDTO.size();   //마지막 장소 저장을 위해 크기 구하기
        int dayIndex = 1;
        String placeIdsOfDay = "";    //place_id 문자열
        planDetail.setPlan(plan);     //매개변수로 받은 plan이 plan_id 가지고있는지 테스트해야함

        for (int i=0;i<size;i++){

            if (planDetailDTO.get(i).getDay() != dayIndex){  //day가 올라가면

                planDetail.setPlace_id(placeIdsOfDay);
                planDetail.setDays(dayIndex);
                planDetailRepository.save(planDetail);

                placeIdsOfDay = "";
                dayIndex++;
            }
            //문자열 뒤에 붙이기
            //문자열 마지막에 ,가 들어가는데 지워야하나-보류
            placeIdsOfDay=placeIdsOfDay+planDetailDTO.get(i).getId()+",";

            if (i+1==size){  //마지막 인덱스
                planDetail.setPlace_id(placeIdsOfDay);
                planDetail.setDays(dayIndex);
                planDetailRepository.save(planDetail);
            }
        }

    }
}