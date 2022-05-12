package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PlanDetail;
import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Repository.PlanDetailRepository;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class PlanController {

    @Autowired //의존성 주입
    private PlanRepository planRepository;

    @Autowired
    private PlanDetailRepository planDetailRepository;

    private final PlanService planService;
    private final UserRepository userRepository;

    public PlanController(PlanService planService, UserRepository userRepository,PlanDetailRepository planDetailRepository) {
        this.planService = planService;
        this.userRepository = userRepository;
        this.planDetailRepository = planDetailRepository;
    }

    @PostMapping("/api/makeSchedule")
    public void plan_save(@RequestBody List<Places> map,@AuthenticationPrincipal User user)
    {
        planService.setPlan_list(map,user);

    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> plan_view(@AuthenticationPrincipal User user, @CookieValue(name = "JSESSIONID", required = false) String jsid, HttpSession httpSession) {
        log.info("요청 세션 값 : " + jsid);
        log.info("서버 서블릿 세션 값 : " + httpSession.getId());
        if (jsid.equals(httpSession.getId())) {
            return ResponseEntity.ok(planService.mypage_planlist(user));
        } else {
            throw new IllegalStateException("계정 정보가 일치하지 않습니다.");
        }
    }
}