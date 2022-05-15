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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    UserInfo userInfo = new UserInfo();


    @PostMapping("/loginSuccess")
    public ResponseEntity<?> LoginSuccess(@AuthenticationPrincipal User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("loginSuccess", true);
        map.put("id", user.getUsername());
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PostMapping("/logoutSuccess")
    public ResponseEntity<?> LogoutSuccess() {
        log.info("로그아웃 성공");
        Map<String, Object> map = new HashMap<>();
        map.put("loginSuccess", true);
        return ResponseEntity.ok(map);
    }


    @ResponseBody
    @PostMapping("/api/register")
    public ResponseEntity<?> SignUp(@RequestBody HashMap<String,Object> map) {
        boolean overlapEmail = userService.usernameOverlap((String) map.get("id"));     //중복 회원검사
        Map<String, Object> jsonMap = new HashMap<>();
        if (overlapEmail == true) {
            jsonMap.put("success",(String) map.get("id"));                               //제이슨에 중복 아이디 담기
        } else if (overlapEmail == false) {
            jsonMap.put("success","success");                                           //제이슨에 성공 담기
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setID((String) map.get("id"));
            userInfo.setPW(bCryptPasswordEncoder.encode((String) map.get("pw")));
            userInfo.setNickName("이름모를 누군가"/*(String) map.get("username")*/);       //닉네임 값 입력 필요함
            userInfo.setAuthority("ROLE_USER");
            userService.join(userInfo);
            userService.Send_Email(userInfo.getID());
        }
        return ResponseEntity.ok(jsonMap);
    }

    @ResponseBody
    @PostMapping("/api/register/email")
    public ResponseEntity<?> ConfirmationEmail(@RequestBody HashMap<String,Object> map) {
        Map<String, Object> jsonMap = new HashMap<>();
        Optional<UserInfo> userinfo1 = userService.Get_UserInfo(userInfo.getID());
        if (userinfo1.get().getCheck_Email().equals((String)map.get("authNum"))) {
//            userService.Update_Userinfo_ROLE_USER(Optional.ofNullable(userInfo));
            jsonMap.put("success",true);
        } else if (!userinfo1.get().getCheck_Email().equals((String)map.get("authNum"))) {
            jsonMap.put("success", false);
        }
        return ResponseEntity.ok(jsonMap);
    }
/*
    //시큐리티 회원가입(해시 함수 적용된 PW암호화)        //리액트와 연동 시 삭제 필요
    @PostMapping("/sign_uppro")
    public String sign_uppro(@Valid UserForm userForm, Errors errors, Model model) {
        if (errors.hasErrors()) {
            */
    /* 회원가입 실패시 입력 데이터 값을 유지 *//*

            model.addAttribute("userDto", userForm);
            */
    /* 유효성 통과 못한 필드와 메시지를 핸들링 *//*

            Map<String, String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            log.info("--뭔가 문제가 있을 때 아래 출력문이 뜸--");
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getUsername());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getPassword());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getUserName_());
            log.info("컨트롤러에서 받은 유저폼의 아이디" + userForm.getLiked());
            */
    /* 회원가입 페이지로 다시 리턴 *//*

            return "/sign_up";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserInfo userInfo = new UserInfo();
        userInfo.setID(userForm.getUsername());
        userInfo.setPW(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userInfo.setNickName(userForm.getUserName_());
//        userInfo.setLiked(userForm.getLiked());
        userInfo.setAuthority("ROLE_USER");
        userService.join(userInfo);
        return "login";
    }
*/

    //회원가입시 아이디 중복체크
/*
    @GetMapping("/api/overlap/usernameRegister")
    public ResponseEntity<?> mapReturn(String username) {

        return ResponseEntity.ok(map);
    }
*/

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