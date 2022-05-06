package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Service.PublicPlanService;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
public class MainController {
    private final UserService userService;
    private final PublicPlanService publicPlanService;

    @Autowired
    public MainController(UserService userService, PublicPlanService publicPlanService) {
        this.userService = userService;
        this.publicPlanService = publicPlanService;
    }

    @PostMapping("/")
    public ResponseEntity<?> index(@AuthenticationPrincipal User user) {
        /*Object principal2 = SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();*/
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getUsername());
        log.info("컨트롤러 유저 아이디 값 :"+user.getUsername());
        return ResponseEntity.ok(map);
    }

    @GetMapping("/123")
    public String index1() {
        return "index";
    }

    @GetMapping("/login")
    public String Login(@AuthenticationPrincipal User user, Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "login";
    }

    @GetMapping("/sign_up")
    public String Sign_up(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
        }
        return "sign_up";
    }

    @GetMapping("/User_Info")
    public String User_Info(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("userInfo", userInfo);
        }
        return "users/User_Info";
    }

    @GetMapping("/map")
    public String map() {
        return "/map";
    }

    @GetMapping("/public_plan")
    public String public_plan() {
       // publicPlanService.view_all_public();
        return "public_plan";
    }
}