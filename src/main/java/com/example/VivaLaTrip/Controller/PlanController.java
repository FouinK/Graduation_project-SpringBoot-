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

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void plan_save(@RequestBody PlanSaveDTO request,
                          @AuthenticationPrincipal User user,
                          @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                          HttpSession httpSession) throws ParseException {

        if (!httpSession.getId().equals(JSESSIONID)) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            throw new IllegalStateException("회원정보가 일치하지 않습니다.");
        }

        String start_date;
        start_date = request.getStart_date().substring(0,4);
        start_date += request.getStart_date().substring(5,7);
        start_date += request.getStart_date().substring(8,10);
        String end_date;
        end_date = request.getEnd_date().substring(0,4);
        end_date += request.getEnd_date().substring(5,7);
        end_date += request.getEnd_date().substring(8,10);

        request.setStart_date(start_date);
        request.setEnd_date(end_date);

        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(start_date);
        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(end_date);
        long diffSec = (format2.getTime()- format1.getTime()) / 1000;
        long total_day = diffSec / (24*60*60)+1;

        List<PlaceComputeDTO> placeComputeDTO = new ArrayList<>();

        for (Places place : request.getCheckedPlace()){
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
        placeComputeDTO = planDetailService.routeCompute(placeComputeDTO, (int) total_day);
        planService.setPlan_list(request,user, placeComputeDTO);

    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> plan_view(@CookieValue(name = "JSESSIONID", required = false) String JSESSIONID, @AuthenticationPrincipal User user, HttpSession httpSession) {
        if (!httpSession.getId().equals(JSESSIONID)) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            throw new IllegalStateException("회원정보가 일치하지 않습니다.");
        }
        log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
        log.info("서버 세션 값: " + httpSession.getId());
        return ResponseEntity.ok(planService.mypage_planlist(user));
    }

    /*@GetMapping("/api/myplan/{plan.planId}")
    public void completeRoute() {
        planDetailService.routeCompute();
    }*/
}
