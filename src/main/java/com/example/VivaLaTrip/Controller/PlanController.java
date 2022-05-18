package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Form.PlaceComputeDTO;
import com.example.VivaLaTrip.Form.PlanDetailResponseDTO;
import com.example.VivaLaTrip.Form.PlanSaveDTO;
import com.example.VivaLaTrip.Form.LoginSuccessPlanListDTO;
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

    @ResponseBody
    @PostMapping("/api/makeSchedule")
    public ResponseEntity<?> plan_save(@RequestBody PlanSaveDTO request,
                          @AuthenticationPrincipal User user,
                          @CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                          HttpSession httpSession) throws ParseException {

        Map<String, Object> map = new HashMap<>();
        //로그인 확인 결과를 담을 Map

        log.info("컨트롤러에서 받자마자 공유 여부 확인 : "+ request.isPublic());

        if (!httpSession.getId().equals(JSESSIONID)||user==null) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            map.put("success", false);
            //로그인 되어있지 않거나 세션값 만료 시 success에 false리턴
            return ResponseEntity.ok(map);
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

        map.put("success", true);
        //로그인 되어있을 때 true리턴

        return ResponseEntity.ok(map);
    }

    @GetMapping("/api/myPageList")
    public @ResponseBody
    ResponseEntity<?> plan_view(@CookieValue(name = "JSESSIONID", required = false) String JSESSIONID,
                                @AuthenticationPrincipal User user,
                                HttpSession httpSession) {

        LoginSuccessPlanListDTO loginSuccessPlanListDTO = new LoginSuccessPlanListDTO();
        //로그인 확인 결과를 담을 SuccessPlanListDTO

        if (!httpSession.getId().equals(JSESSIONID) || user == null) {
            log.info("프론트 부터 받아온 세션 값: " + JSESSIONID);
            log.info("서버 세션 값: " + httpSession.getId());
            loginSuccessPlanListDTO.setLoginSuccess(false);
            //로그인 되어있지 않거나 세션값 만료 시 success에 false리턴
            return ResponseEntity.ok(loginSuccessPlanListDTO);
        }

        loginSuccessPlanListDTO.setLoginSuccess(true);
        loginSuccessPlanListDTO.setPlanListDTOList(planService.mypage_planlist(user));
        //로그인 되어있을 시 PlanList와 success값 트루 리턴 (석세스가 한겹 더 위에 있음)
        return ResponseEntity.ok(loginSuccessPlanListDTO);
    }


    @GetMapping( "/api/myPlan")
    public @ResponseBody
    ResponseEntity<?> responsePlanDetail(@RequestParam("planId") Long planId) {
        log.info(planId + "번 plan 요청받음");
        PlanDetailResponseDTO response = planService.getPlanDetail(planId);

        return ResponseEntity.ok(response);
    }

    /*@GetMapping("/api/myplan/{plan.planId}")
    public void completeRoute() {
        planDetailService.routeCompute();
    }*/
}