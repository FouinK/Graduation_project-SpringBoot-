package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.PlanResponseDto;
import com.example.VivaLaTrip.Service.PlanService;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
public class MainController {
    private final UserService userService;
    private final PlanService planService;

    @Autowired
    public MainController(UserService userService, PlanService planService) {
        this.userService = userService;
        this.planService = planService;
    }

    @GetMapping("/")
    public String index(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
        }
        return "index";
    }

/*    @GetMapping("/login")
    public String Login(@AuthenticationPrincipal User user, Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "exception", required = false) String exception) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
        }
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);

        return "login";
    }*/

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
    public String public_plan(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("userInfo", userInfo);
        }
        model.addAttribute("plan", planService.findAllDesc());
        return "public_plan";//경로,확장자 자동
    }

    @GetMapping("/plan/save")
    public String postsSave(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("userInfo", userInfo);
        }
        return "plan_save";
    }

    @GetMapping("/plan/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PlanResponseDto dto = planService.findById(id);
        model.addAttribute("plan", dto);
        return "plan_update";
    }
}