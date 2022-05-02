package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Service.PublicPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PublicPlanController {

    PublicPlanService publicPlanService;

    @Autowired
    public PublicPlanController(PublicPlanService publicPlanService){
        this.publicPlanService = publicPlanService;
    }

    public void public_plan_all(){
        publicPlanService.view_all_public();
    }

}
