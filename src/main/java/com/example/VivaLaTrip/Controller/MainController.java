package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Service.PublicPlanService;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

/*    @GetMapping("/public_plan")
    public String public_plan() {
       // publicPlanService.view_all_public();
        return "public_plan";
    }*/
}