package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class PlanController {

    @Autowired //의존성 주입
    private PlanRepository planRepository;

    private final PlanService planService;
    private final UserRepository userRepository;

    public PlanController(PlanService planService, UserRepository userRepository) {
        this.planService = planService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/makeSchedule")
    public void plan_save(@RequestBody List<Places> map,@AuthenticationPrincipal User user)
    {
        planService.setPlan_list(map,user);

    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> plan_view(@AuthenticationPrincipal User user)
    {
        return ResponseEntity.ok(planService.mypage_planlist(user));
    }

    /*@GetMapping("/api/myplan/{plan.plan}")
    public plan_detail()
    {

    }*/
}
