package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Repository.UserRepository;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class VivaLaTripConfig {
    private final UserRepository userRepository;

    @Autowired
    public VivaLaTripConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserService userService() {
        return new UserService(userRepository);
    }
}