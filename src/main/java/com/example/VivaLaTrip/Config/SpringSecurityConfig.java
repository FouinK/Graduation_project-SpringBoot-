package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.HashMap;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    /* 로그인 실패 핸들러 의존성 주입 */
    private final AuthenticationFailureHandler customFailurHandler;

    @Override
    @ResponseBody
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()           //포스트 매핑 오류 제거
                .authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**","/users/**").permitAll()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN");    //역할에 따라 접근 통제 가능
//                .antMatchers("/users").hasAuthority("ROLE_USER");

//                .anyRequest().authenticated()                           //어떠한 URL로 접근하던지 인증이 필요함
//        http
//                .authorizeRequests()
//                .anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/api/login")                          //로그인 페이지 호출
                .permitAll()
//                .loginProcessingUrl("/api/login")                   //폼으로 받는 URL
                .usernameParameter("id")                            //아이디 파라미터 받기
                .passwordParameter("pw")                           //비밀번호 파라미터 받기
                .failureHandler(customFailurHandler) // 로그인 실패 핸들러
//                .defaultSuccessUrl("/",true)        //로그인 성공시 이동할 페이지
//                         .failureUrl("/users/login")
////                         .permitAll()
                .and()
                .logout()                                    //로그아웃
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true);
    //세션 날리기(?)
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logoutpro"));     //로그아웃
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/img/**","/h2-console/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

/*
    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}