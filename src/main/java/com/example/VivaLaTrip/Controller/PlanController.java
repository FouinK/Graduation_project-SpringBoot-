package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Form.PlaceComputeDTO;
import com.example.VivaLaTrip.Form.PlanSaveDTO;
import com.example.VivaLaTrip.Service.PlanDetailService;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Slf4j
@RestController
public class PlanController {

    PlanDetailService planDetailService;
    PlanService planService;

    @Autowired
    public PlanController(PlanDetailService planDetailService, PlanService planService) {
        this.planDetailService = planDetailService;
        this.planService = planService;
    }

    @PostMapping("/api/makeSchedule")
    public void plan_save(@RequestBody PlanSaveDTO map, @AuthenticationPrincipal User user) {

        log.info(String.valueOf(map));
        log.info(map.getCheckedPlace().get(0).toString());

        List<PlaceComputeDTO> placeComputeDTO = new ArrayList<>();

        for (Places place : map.getCheckedPlace()){
            PlaceComputeDTO placeItem = PlaceComputeDTO.builder()
                    .id(place.getId())
                    .x(Double.parseDouble(place.getX()))
                    .y(Double.parseDouble(place.getY()))
                    .stay(place.getStay())
                    .days(0)
                    .slope(0)
                    .where("")
                    .build();
            placeComputeDTO.add(placeItem);
        }
        placeComputeDTO = planDetailService.routeCompute(placeComputeDTO);
        planService.setPlan_list(map,user, placeComputeDTO);

    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> plan_view(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(planService.mypage_planlist(user));
    }

    /*@GetMapping("/api/myplan/{plan.planId}")
    public void completeRoute() {
        planDetailService.routeCompute();
    }*/
}
