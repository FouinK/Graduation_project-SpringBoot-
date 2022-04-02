package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Service.PlanService;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VivaLaTripConfig {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    @Autowired
    public VivaLaTripConfig(UserRepository userRepository, PlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }

    @Bean
    public PlanService planService() {
        return new PlanService(planRepository);
    }
}