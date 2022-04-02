package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.UserForm;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //시큐리티 회원가입(해시 함수 적용된 PW암호화)
    @PostMapping("/sign_uppro")
    public String sign_uppro(@Valid UserForm userForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            /* 회원가입 실패시 입력 데이터 값을 유지 */
            model.addAttribute("userDto", userForm);
            /* 유효성 통과 못한 필드와 메시지를 핸들링 */
            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getUsername());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getPassword());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getUserName_());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getLiked());
            /* 회원가입 페이지로 다시 리턴 */
            return "/sign_up";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserInfo userInfo = new UserInfo();
        userInfo.setID(userForm.getUsername());
        userInfo.setPW(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userInfo.setNickName(userForm.getUserName_());
        userInfo.setLiked(userForm.getLiked());
        userInfo.setAuthority("ROLE_TEMPORARY_USER");
        userService.join(userInfo);
        return "login";
    }

    //회원가입시 아이디 중복체크
    @GetMapping("/api/overlap/usernameRegister")
    public ResponseEntity<?> mapReturn(String username) {
        HashMap<String, Object> map = userService.usernameOverlap(username);
        return ResponseEntity.ok(map);
    }

    @PostMapping("/Email_Check")
    public String Email_Check(@AuthenticationPrincipal User user, Model model) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
            userService.Send_Email(user.getUsername());
            log.info("컨트롤러에서 받은 email값 : " + user.getUsername());
        }
        return "test";
    }

    @PostMapping("/Email_Checkpro")
    public String Email_Checkpro(@AuthenticationPrincipal User user, Model model, String check_email) {
        if (user != null) {     //유저정보 보내기
            Optional<UserInfo> userInfo = userService.Get_UserInfo(user.getUsername());
            model.addAttribute("username", userInfo.get().getNickName());
            if (userInfo.get().getCheck_Email().equals(check_email)) {
                userService.Update_Userinfo_ROLE_USER(userInfo);
                log.info("컨트롤러에서 받은 이메일 :"+check_email);
                log.info("컨트롤러에서 받은 유저인포의 이메일 :"+userInfo.get().getCheck_Email());
                log.info("이메일 인증 성공");
                return "index";
            }
//            log.info("컨트롤러에서 받은 이메일 :"+check_email);
//            log.info("컨트롤러에서 받은 유저인포의 이메일 :"+userInfo.get().getCheck_Email());
        }
        log.info("이메일 인증 실패");
        return "test";
    }
}