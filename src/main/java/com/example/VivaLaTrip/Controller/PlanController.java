package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.PlaceComputeDTO;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Service.PlanDetailService;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void plan_save(
            @RequestBody List<Places> map,
            @AuthenticationPrincipal User user,
            @RequestParam("start_date") String start_date,
            @RequestParam("end_date") String end_date)
    {
        List<PlaceComputeDTO> listDTO = new ArrayList<>();
        log.info(start_date+end_date);
        /*for (Places a: map){
            PlaceComputeDTO placeItem = PlaceComputeDTO.builder()
                    .x(Double.parseDouble(a.getX()))
                    .y(Double.parseDouble(a.getY()))
                    .stay(2)
                    .days(map.size())
                    .slope(0)
                    .where("")
                    .build();
            listDTO.add(placeItem);
        }
        planService.setPlan_list(map,user);
        planDetailService.routeCompute(listDTO);*/
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
