/*
package com.example.VivaLaTrip.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    */
/*
     * 개발환경에서의 크로스 도메인 이슈 해결을 위한 코드. 운영 배포 시 14~15행 주석
     *//*

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowCredentials(true).allowedOrigins("http://localhost:3000");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}*/
