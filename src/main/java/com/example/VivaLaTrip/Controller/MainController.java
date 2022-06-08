package com.example.VivaLaTrip.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MainController {
    @GetMapping("*")
    public String goIndex() {
        return "index";
    }
}