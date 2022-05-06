package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.PublicPlan;
import com.example.VivaLaTrip.Service.PublicPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class PublicPlanController {

    PublicPlanService publicPlanService;

    @Autowired
    public PublicPlanController(PublicPlanService publicPlanService){
        this.publicPlanService = publicPlanService;
    }

    @PostMapping("/get_all_public")
    public void getAllPublic(){
        List<PublicPlan> plans = publicPlanService.viewAllPublic();

        for (PublicPlan publicPlan : plans) {
            log.info("일정 ID : " + publicPlan.getPlanId().toString() +
                    ", 일정 코멘트 : " + publicPlan.getComment() +
                    ", 일정 Like : " + String.valueOf(publicPlan.getLike_count()));
        }

    }

    @PostMapping("/get_plan")
    public String getPlanById(@RequestParam("id") Long plan_id){

        log.info("입력받은 일정 ID : "+plan_id.toString());
        publicPlanService.findPlan(plan_id);
        return "";
    }

    @PostMapping("/to_public_plan")
    public String makePublicPlanById(
            @RequestParam("id") Long plan_id,
            @RequestParam("comment") String comment,
            @AuthenticationPrincipal User user){

        log.info("입력받은 일정 ID : "+plan_id.toString());
        publicPlanService.toPublic(plan_id, comment, user);
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