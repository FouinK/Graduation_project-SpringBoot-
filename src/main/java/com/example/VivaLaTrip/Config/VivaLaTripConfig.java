package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Repository.PlanDetailRepository;
import com.example.VivaLaTrip.Repository.PlanRepository;
import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Repository.MapRepository;
import com.example.VivaLaTrip.Service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class VivaLaTripConfig {

    private final UserRepository userRepository;
    private final MapRepository mapRepository;
    private final PlanRepository planRepository;
    private final PlanDetailRepository planDetailRepository;

    @Autowired
    public VivaLaTripConfig(UserRepository userRepository, MapRepository mapRepository, PlanRepository planRepository, PlanDetailRepository planDetailRepository) {
        this.userRepository = userRepository;
        this.mapRepository = mapRepository;
        this.planRepository = planRepository;
        this.planDetailRepository = planDetailRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
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