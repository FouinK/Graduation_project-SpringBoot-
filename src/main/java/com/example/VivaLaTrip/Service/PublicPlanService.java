package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.*;
import com.example.VivaLaTrip.Form.PagePublicPlanListDTO;
import com.example.VivaLaTrip.Form.PlanListDTO;
import com.example.VivaLaTrip.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PublicPlanService {
    private final PublicPlanRepository publicPlanRepository;
    private final PlanRepository planRepository;
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;
    private final PlanDetailRepository planDetailRepository;

    public List<PublicPlan> viewAllPublic() {
        return publicPlanRepository.findAll();
    }

    public Plan findPlan(Long plan_id) {

        Plan plan = planRepository.findByPlanId(plan_id);

        log.info("일정 ID : " + plan.getPlanId().toString());
        log.info("유저 ID : " + plan.getUserInfo().getUserId().toString());
        log.info("총 장소 : " + plan.getTotal_count());
        log.info("시작날 : " + plan.getStart_date());
        log.info("끝날 : " + plan.getEnd_date());

        return plan;
    }

    public PagePublicPlanListDTO matchPlans(Pageable pageable) {

        List<PlanListDTO> listDTO = new ArrayList<>();
        //전체 페이지 개수 안에 플랜 리스트 담기 위해
        Page<PublicPlan> publicPlan = publicPlanRepository.findAll(pageable);
        //페이지 담기 위해

        for (PublicPlan pp : publicPlan) {
            Plan plan = planRepository.findByPlanId(pp.getPlanId());

            PlanListDTO publicPlanListItem = PlanListDTO.builder()
                    .userId(plan.getUserInfo().getUserId().toString())
                    .start_date(plan.getStart_date())
                    .end_date(plan.getEnd_date())
                    .plan_id(plan.getPlanId().toString())
                    .title(pp.getComment())
                    .place_num(plan.getTotal_count())
                    .liked(pp.getLike_count())
                    .build();

            listDTO.add(publicPlanListItem);

        }

        PagePublicPlanListDTO loginSuccessPlanListDTO = new PagePublicPlanListDTO();
        loginSuccessPlanListDTO.setTotalPage(publicPlan.getTotalPages());
        //총 페이지 개수 담음
        loginSuccessPlanListDTO.setOthersPlans(listDTO);
        //플랜리스트 담음
        return loginSuccessPlanListDTO;
    }

    public void toPublic(Long plan_id, String comment) {
        log.info("ssd");
        Plan plan = findPlan(plan_id);
        PublicPlan publicPlan = new PublicPlan();

        publicPlan.setPlan(plan);
        publicPlan.setPlanId(plan_id);
        publicPlan.setComment(comment);
        publicPlan.setLike_count(0);
        publicPlanRepository.save(publicPlan);

        plan.setIs_public(true);
        planRepository.save(plan);
        log.info(plan_id + "번 일정 공유");

        /*if (user.getUsername().equals(plan.getUserInfo().getUsername())){  //해당일정이 자신것인지 확인
            if (!plan.getIs_public()){  //공유되어있지 않음
                publicPlan.setPlan(plan);
                publicPlan.setPlanId(plan_id);
                publicPlan.setComment(comment);
                publicPlan.setLike_count(0);
                publicPlanRepository.save(publicPlan);

                plan.setIs_public(true);
                planRepository.save(plan);
                log.info(plan_id+"번 일정 공유");
            }else {
                log.info("해당 일정은 이미 공유 됨");
            }
        }else {
            log.info("해당 일정은 니꺼 아님");
        }*/
    }

    public void toPrivate(long plan_id, User user) {

        Plan plan = findPlan(plan_id);

        boolean isLiked = likedRepository.existsByPlan_PlanId(plan_id);

        if (user.getUsername().equals(plan.getUserInfo().getUsername())) {
            if (plan.getIs_public()) {
                plan.setIs_public(false);
                PublicPlan publicPlan = publicPlanRepository.findByPlanId(plan_id);
                publicPlan.setPlan(null);   //참조키 관계 파괴-안하면 plan도 같이 삭제됨
                publicPlanRepository.delete(publicPlan);
                log.info(plan_id + "번 일정 공유 취소");
                if (isLiked) {   //좋아요 테이블 비우기
                    List<Liked> liked = likedRepository.findLikedsByPlan_PlanId(plan_id);
                    likedRepository.deleteAll(liked);
                    log.info(plan_id + "번 일정 좋아요 모두 삭제");
                }
            } else {
                log.info("해당 일정을 찾을 수 없음");
            }
        } else {
            log.info("해당 일정은 니꺼 아님");
        }
    }

    public String addLike(Long plan_id, User user) {

        String key;
        //overlap또는 success결과를 담을 문자열
        Liked liked = new Liked();
        PublicPlan publicPlan = publicPlanRepository.findByPlanId(plan_id);
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());

        boolean pushed = likedRepository.existsByPlan_PlanIdAndUserInfo_UserId(plan_id, userInfo.get().getUserId());
        boolean cancleLike = false;
        //좋아요 취소 기능이 생기면 동작할 변수

        if (pushed) {
            log.info("이미 '좋아요'를 누른 일정입니다.");
            key = "overlap";
        } else {
            //public에서 라이크 올리기
            publicPlan.setLike_count(publicPlan.getLike_count() + 1);
            publicPlanRepository.save(publicPlan);

            //라이크 테이블 생성
            liked.setUserInfo(userInfo.get());
            liked.setPlan(publicPlan.getPlan());
            likedRepository.save(liked);
            log.info(userInfo.get().getUserId() + "번 회원이 " + plan_id + "번 일정에 좋아요");
            key = "success";
        }

        if (cancleLike) {
            if (pushed) {
                //Public에서 liked -1 하고 저장
                publicPlan.setLike_count(publicPlan.getLike_count() - 1);
                publicPlanRepository.save(publicPlan);

                //Liked 해당 row 삭제
                Liked like1 = likedRepository.findByPlan_PlanIdAndUserInfo_UserId(plan_id, userInfo.get().getUserId());
                likedRepository.deleteById(like1.getNum());
                log.info(userInfo.get().getUserId() + "번 회원이 " + plan_id + "번 일정에 좋아요 취소");
            } else {
                log.info("좋아요 안누름");
            }
        }
        return key;
    }

    public String toMyplan(Long plan_id, User user) {
        String key;
        //결과 자열을 담을 key
        Optional<UserInfo> userInfo = userRepository.findByID(user.getUsername());
        Plan plan = planRepository.findByPlanId(plan_id);
        //existsByPlanIdAndUserInfo_UserId으로 했다가 두번 참조해야해서 파인드바이로 사용
        boolean plancheck = planRepository.existsByFromPlanIdAndUserInfo_UserId(plan_id,userInfo.get().getUserId());
        //이미 가져온 일정인지 확인 하기 위해

        if (plan.getUserInfo().getUserId() == userInfo.get().getUserId() || plancheck==true ) {
            //해당 publicPlan이 myPlan인지  검사, 이미 가져온 일정인지 검사
            key = "overlap";
        } else {
            Plan newMyplan = new Plan();
            newMyplan.setUserInfo(userInfo.get());
            newMyplan.setIs_public(false);
            newMyplan.setTotal_count(plan.getTotal_count());
            newMyplan.setStart_date(plan.getStart_date());
            newMyplan.setEnd_date(plan.getEnd_date());
            newMyplan.setFromPlanId(plan_id);
            planRepository.save(newMyplan);
            //새로운 plan저장

            newMyplan = planRepository.findByPlanId(newMyplan.getPlanId());
            //새롭게 저장된 plan가져오기
            List<PlanDetail> planDetailList = planDetailRepository.findAllByPlan_PlanId(plan.getPlanId());
            int size = planDetailList.size();
            PlanDetail[] newplanDetailarray = new PlanDetail[size];
            //새롭게 담을 PlanDetail
            //어레이리스트 캐퍼시티 적용 되지 않아서 클래스 배열로 생성

            for (int i = 0; i < newplanDetailarray.length; i++) {
                //입력받은 planId의 디테일 가져오기 새로운 디테일을 만들기 위한 목적
                newplanDetailarray[i] = new PlanDetail();
                newplanDetailarray[i].setPlan(newMyplan);
                newplanDetailarray[i].setDays(planDetailList.get(i).getDays());
                newplanDetailarray[i].setPlace_id(planDetailList.get(i).getPlace_id());
            }

            List<PlanDetail> newplanDetailList = Arrays.asList(newplanDetailarray);
            //어레이 리스트로 변환

            for (int i = 0; i < size; i++) {
                planDetailRepository.save(newplanDetailList.get(i));
                //새로운 플랜 디테일 담기
            }
            key = "success";
        }
        return key;
    }
}