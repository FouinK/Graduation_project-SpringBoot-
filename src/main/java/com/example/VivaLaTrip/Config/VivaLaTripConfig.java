package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Repository.MapRepository;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Service.MapService;
import com.example.VivaLaTrip.Service.PlanService;
import com.example.VivaLaTrip.Service.UserService;
import com.example.VivaLaTrip.Service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VivaLaTripConfig {

    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final MapRepository mapRepository;

    @Autowired
    public VivaLaTripConfig(UserRepository userRepository, PlanRepository planRepository, MapRepository mapRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.mapRepository = mapRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }

    @Bean
    public PlanService planService() {
        return new PlanService(planRepository);
    }

    @Bean
    public WeatherService weatherService() {
        return new WeatherService();
    }

    @Bean
    public MapService mapService() {
        return new MapService(mapRepository);
    }
}