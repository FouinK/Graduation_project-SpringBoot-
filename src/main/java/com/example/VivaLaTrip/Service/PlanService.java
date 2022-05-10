package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PublicPlan;
import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.PlanListDTO;
import com.example.VivaLaTrip.Form.PlanRequestDto;
import com.example.VivaLaTrip.Repository.LikedRepository;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.PublicPlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
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
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;


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
    }

    public List<PlanListDTO> mypage_planlist(User user) {
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        //로그인 한 사람의 정보를 userInfo에 넣음

        List<PlanListDTO> listDTO = new ArrayList<>();
        //결과를 담을 DTO list 선언(front로 보내줄 거)

        List<Plan> user_plan = planRepository.findAllByUserInfo_UserId(userInfo.get().getUserId());
        //userinfo에서 id값 가져온거 plan리포지토리 userId에서 찾음= plan이 있는 값 가져옴

        List<PublicPlan> publicPlan = new ArrayList<>();

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
}