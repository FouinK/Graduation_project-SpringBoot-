package com.example.VivaLaTrip.Config;

import com.example.VivaLaTrip.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    HttpSession httpSession;

    //로그인 동작이 실제로 일어나는 함수(시큐리티 클래스)
    @Override
    @ResponseBody
    public void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/**", "/users/**").permitAll()
                .antMatchers("/admin").hasAuthority("ROLE_ADMIN");    //역할에 따라 접근 통제 가능
        http
                .formLogin()
                .permitAll()
                .loginPage("/login")
                .loginProcessingUrl("/api/login")                  //로그인 요청을 받는 url
                .usernameParameter("id")                           //url내부 body값에 저장 된(프론트에서 입력 된) id값과 일치여부 확인
                .passwordParameter("pw")                           //url내부 body값에 저장 된(프론트에서 입력 된) pw값과 일치여부 확인
                .successForwardUrl("/loginSuccess")                //로그인 성공시 이동할 Mapping url
                .failureForwardUrl("/loginFail")                   // 로그인 실패시 이동할 Mapping url
                .and()
                .logout()               //실제 로그인이 처리되는 로그아웃 함수
                .logoutUrl("/api/logout")                           //로그아웃 요청을 받는 url
                .logoutSuccessUrl("/logoutSuccess")                 //로그아웃 성공시 이동할 Mapping url
                .invalidateHttpSession(true);                       //로그아웃 시 로그인 시 부여됐던 Session값 삭제
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/img/**","/h2-console/**");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    //패스워드 암호화 함수
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}