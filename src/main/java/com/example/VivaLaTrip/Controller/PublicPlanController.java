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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class PublicPlanController {

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
        log.info("public_plan값 확인 : " + plans.getTotalPage());
        log.info("public_plan값 확인 : " + plans.getOthersPlans());

        return ResponseEntity.ok(plans);
    }

    @GetMapping("/api/publicPlan")
    public @ResponseBody
    ResponseEntity<?> responsePublicPlanDetail(@RequestParam("planId") Long planId) {

        log.info(planId + "번 plan 요청받음");
        PlanDetailResponseDTO response = planService.getPlanDetail(planId);
//        response.setLoginSuccess(true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/publicPlan/liked")
    public @ResponseBody
    ResponseEntity<?> likePublicPlan(@AuthenticationPrincipal User user,
                                     @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                     HttpSession httpSession) {

        Map<String, Object> map = new HashMap<>();
        //로그인 확인 결과를 담을 Map

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            //로그인 되어있지 않다면
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            map.put("result","login");
            //value = login 리턴
            return ResponseEntity.ok(map);
        }

        if (true) {         //좋아요를 이미 눌렀다면
            map.put("result", "overlap");
            //value = overlap 리턴
            return ResponseEntity.ok(map);
        }

//        log.info("입력받은 일정 ID : "+plan_id.toString());
//        publicPlanService.addLike(plan_id, like, user);
        //일정번호랑 유저를 입력 받아야함

        return ResponseEntity.ok(map);
    }

    @GetMapping("/api/publicPlan/copy")
    public @ResponseBody
    ResponseEntity<?> copyPublicPlan(@AuthenticationPrincipal User user,
                                     @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                     HttpSession httpSession) {

        Map<String, Object> map = new HashMap<>();
        //로그인 확인 결과를 담을 Map

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            //로그인 되어있지 않다면
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            map.put("result","login");
            //value = login 리턴
            return ResponseEntity.ok(map);
        }

        if (true) {         //이미 가져온 일정이라면
            map.put("result", "overlap");
            //value = overlap 리턴
            return ResponseEntity.ok(map);
        }

        return ResponseEntity.ok(map);
    }

    @PostMapping("/get_plan")
    public String getPlanById(@RequestParam("id") Long plan_id) {

        log.info("입력받은 일정 ID : " + plan_id.toString());
        publicPlanService.findPlan(plan_id);
        return "";
    }

    @PostMapping("/to_public_plan")
    public String makePublicPlanById(
            @RequestParam("id") Long plan_id,
            @RequestParam("comment") String comment,
            @AuthenticationPrincipal User user){

        log.info("입력받은 일정 ID : "+plan_id.toString());
        //publicPlanService.toPublic(plan_id, comment, user);
        return "";
    }

    @PostMapping("/delete_public_plan")
    public String deletePublicPlanById(
            @RequestParam("id") Long plan_id,
            @AuthenticationPrincipal User user){

        log.info("입력받은 일정 ID : "+plan_id.toString());
        publicPlanService.toPrivate(plan_id, user);
        return "";
    }

    @PostMapping("/like")
    public String likePublicPlanById(
            @RequestParam("id") Long plan_id,
            @RequestParam("like") String like,
            @AuthenticationPrincipal User user){

        log.info("입력받은 일정 ID : "+plan_id.toString());
        publicPlanService.addLike(plan_id, like, user);
        return "";
    }
}