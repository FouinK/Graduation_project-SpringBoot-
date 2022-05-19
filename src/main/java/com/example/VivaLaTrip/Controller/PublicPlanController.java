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