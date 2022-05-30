package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Service.MapService;
import com.example.VivaLaTrip.Service.PubTransService;
import com.example.VivaLaTrip.Service.PublicPlanService;
import com.example.VivaLaTrip.Service.UserService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MainController {
    private final UserService userService;
    private final PublicPlanService publicPlanService;
    private final PubTransService pubTransService;
    private final MapService mapService;

    @Autowired
    public MainController(UserService userService, PublicPlanService publicPlanService, PubTransService pubTransService, MapService mapService) {
        this.userService = userService;
        this.publicPlanService = publicPlanService;
        this.pubTransService = pubTransService;
        this.mapService = mapService;
    }

    @GetMapping("/")
    public String toIndex() throws UnirestException {
        return "index";
    }
}