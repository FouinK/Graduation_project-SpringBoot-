package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Form.PagePublicPlanListDTO;
import com.example.VivaLaTrip.Form.PlanDetailResponseDTO;
import com.example.VivaLaTrip.Service.PlanService;
import com.example.VivaLaTrip.Service.PublicPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class PublicPlanController {
    Long planId;
    //플랜 아이디 담을 전역 변수
    PublicPlanService publicPlanService;
    PlanService planService;

    @Autowired
    public PublicPlanController(PublicPlanService publicPlanService, PlanService planService){
        this.publicPlanService = publicPlanService;
        this.planService = planService;
    }

    @GetMapping("/api/publicPageList")
    public @ResponseBody
    ResponseEntity<?> getAllPublic(@PageableDefault(size = 10) Pageable pageable){
        PagePublicPlanListDTO plans = publicPlanService.matchPlans(pageable);

        return ResponseEntity.ok(plans);
    }

    @GetMapping("/api/publicPlan")
    public @ResponseBody
    ResponseEntity<?> responsePublicPlanDetail(@RequestParam("planId") Long getPlanId,@AuthenticationPrincipal User user) {
        planId = getPlanId;
        PlanDetailResponseDTO response = planService.getPlanDetail(getPlanId, user, "public_plan");     //user파라미터 추가는 myplan의 내일정인지 조회하기 위해 보내야함 (같은 메소드 사용해서)

        response.setTotal_days(response.getPlaces().get(response.getPlace_num()-1).getDay());

        response.setLoginSuccess(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/publicPlan/liked")
    public @ResponseBody
    ResponseEntity<?> likePublicPlan(@AuthenticationPrincipal User user,
                                     @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                     HttpSession httpSession) {

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            //로그인 되어있지 않다면
            //value = login 리턴
            return ResponseEntity.ok("login");
        }

        log.info("입력받은 일정 ID : "+planId);
        String key = publicPlanService.addLike(planId, user);
        //문자열로 담아서 송신.

        return ResponseEntity.ok(key);
    }

    @GetMapping("/api/publicPlan/copy")
    public @ResponseBody
    ResponseEntity<?> copyPublicPlan(@AuthenticationPrincipal User user,
                                     @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                     HttpSession httpSession) {
        String key;
        //결과를 담을 key
        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            //로그인 되어있지 않다면
            key = "login";
            return ResponseEntity.ok(key);
        }

        key = publicPlanService.toMyPlan(planId, user);

        return ResponseEntity.ok(key);
    }
}