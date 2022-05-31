package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Form.*;
import com.example.VivaLaTrip.Service.MapService;
import com.example.VivaLaTrip.Service.PlanDetailService;
import com.example.VivaLaTrip.Service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    //전역변수로 플랜아이디 저장
    Long planId;

    PlanDetailService planDetailService;
    PlanService planService;

    MapService mapService;

    @Autowired
    public PlanController(PlanDetailService planDetailService, PlanService planService, MapService mapService) {
        this.planDetailService = planDetailService;
        this.planService = planService;
        this.mapService = mapService;
    }

    @ResponseBody
    @PostMapping("/api/makeSchedule")
    public ResponseEntity<?> responseMakePlan(@RequestBody PlanSaveDTO request,
                                              @AuthenticationPrincipal User user,
                                              @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                              HttpSession httpSession) throws ParseException {
        //로그인 확인 결과를 담을 Map
        Map<String, Object> map = new HashMap<>();

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            //로그인 되어있지 않거나 세션값 만료 시 success에 false리턴
            map.put("success", false);
            return ResponseEntity.ok(map);
        }

        String start_date;
        start_date = request.getStart_date().substring(0, 4);
        start_date += request.getStart_date().substring(5, 7);
        start_date += request.getStart_date().substring(8, 10);
        String end_date;
        end_date = request.getEnd_date().substring(0, 4);
        end_date += request.getEnd_date().substring(5, 7);
        end_date += request.getEnd_date().substring(8, 10);

        request.setStart_date(start_date);
        request.setEnd_date(end_date);

        Date format1 = new SimpleDateFormat("yyyyMMdd").parse(start_date);
        Date format2 = new SimpleDateFormat("yyyyMMdd").parse(end_date);
        long diffSec = (format2.getTime() - format1.getTime()) / 1000;
        long total_day = diffSec / (24 * 60 * 60) + 1;

        if (total_day * 1.5 > request.getCheckedPlace().size()) {
            request.setCheckedPlace(mapService.placeAdd(request.getCheckedPlace(), total_day * 1.5));
        }

        List<PlaceComputeDTO> placeComputeDTO = new ArrayList<>();

        for (Places place : request.getCheckedPlace()) {
            PlaceComputeDTO placeItem = PlaceComputeDTO.builder()
                    .id(place.getId())
                    .x(place.getX())
                    .y(place.getY())
                    .stay(place.getStay())
                    .days(0)
                    .slope(0)
                    .where("")
                    .build();
            placeComputeDTO.add(placeItem);
        }
        placeComputeDTO = planDetailService.routeCompute(placeComputeDTO, (int) total_day);
        planService.savePlan(request, user, placeComputeDTO);

        //로그인 되어있을 때 true리턴
        map.put("success", true);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> responseMyPage(@CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                     @PageableDefault(size = 10) Pageable pageable,
                                     @AuthenticationPrincipal User user,
                                     HttpSession httpSession) {

        //로그인 확인 결과를 담을 SuccessPlanListDTO
        LoginSuccessPlanListDTO loginSuccessPlanListDTO = new LoginSuccessPlanListDTO();

        log.info("페이지에이블 값 확인" + pageable.toString());

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            //로그인 되어있지 않거나 세션값 만료 시 success에 false리턴
            loginSuccessPlanListDTO.setLoginSuccess(false);
            return ResponseEntity.ok(loginSuccessPlanListDTO);
        }

        //로그인 되어있을 시 PlanList와 success값 트루 리턴 (석세스가 한겹 더 위에 있음)
        loginSuccessPlanListDTO = planService.getMyPlans(user, pageable);
        return ResponseEntity.ok(loginSuccessPlanListDTO);
    }


    @GetMapping("/api/myPlan")
    public @ResponseBody
    ResponseEntity<?> responsePlanDetail(@RequestParam("planId") Long getplanId,
                                         @AuthenticationPrincipal User user,
                                         @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                         HttpSession httpSession) {

        Map<String, Object> map = new HashMap<>();
        //로그인 확인 결과를 담을 Map

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            //로그인 되어있지 않거나 세션값 만료 시 success에 false리턴
            map.put("loginSuccess", false);
            return ResponseEntity.ok(map);
        }
        //전역변수에 담기
        planId = getplanId;
        log.info(planId + "번 plan 요청받음");
        PlanDetailResponseDTO response = planService.getPlanDetail(planId, user);
        response.setLoginSuccess(true);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/editPlan")
    public @ResponseBody
    ResponseEntity<?> responseBeforeEditPlanDetail() {
 /*       log.info(planId + "번 plan 요청받음");
        PlanDetailResponseDTO response = planService.getPlanDetail(planId, user);
        response.setLoginSuccess(true);*/
        return ResponseEntity.ok("success");
    }

    @PostMapping("/api/edit")
    public @ResponseBody
    ResponseEntity<?> responseCompleteEditPlanDetail(@RequestBody UpdatePlanDTO updatePlanDTO,
                                                     @AuthenticationPrincipal User user) {
        planService.updateMyPlan(updatePlanDTO,planId,user);

        return ResponseEntity.ok("Ok");
    }
}