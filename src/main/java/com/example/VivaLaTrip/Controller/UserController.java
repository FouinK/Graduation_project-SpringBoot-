package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.UserForm;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*로그인 실패, 로그인 성공, 로그아웃 성공, 회원가입의 동작들이 작동 할 수 있게 도와주는 다양한 Mapping 존재
UserController -> 모든 Mapping은 프론트와 직접적인 송수신이 일어남*/
@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    //이메일 검사를 위한 유저 아이디 저장
    String username;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //SpringSecurity에서 로그인 실패를 반환했을 때 매핑 되는 함수이며
    @PostMapping("/loginFail")
    public ResponseEntity<?> LoginFail() {
        Map<String, Object> map = new HashMap<>();
        System.out.println("여기가 작동 ?");
        map.put("loginSuccess", false);
        return ResponseEntity.ok(map);
    }

    //SpringSecurity에서 로그인 성공을 반환했을 때 매핑 되는 함수
    @PostMapping("/loginSuccess")
    public ResponseEntity<?> LoginSuccess(@AuthenticationPrincipal User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("loginSuccess", true);
        map.put("id", user.getUsername());
        return ResponseEntity.ok(map);
    }

    //SpringSecurity에서 로그아웃 성공을 반환했을 때 매핑 되는 함수
    @ResponseBody
    @ResponseStatus
    @GetMapping("/logoutSuccess")
    public ResponseEntity<Object> LogoutSuccess() throws URISyntaxException {
        log.info("로그아웃 성공");
        Map<String, Object> map = new HashMap<>();
        map.put("loginSuccess", true);
        return ResponseEntity.ok(map);
    }

    //회원가입 로직을 처리하는 매핑 함수
    @ResponseBody
    @PostMapping("/api/register")
    public ResponseEntity<?> SignUp(@RequestBody HashMap<String,Object> map) {
        UserInfo userInfo = new UserInfo();
        username = (String) map.get("id");
        boolean overlapEmail = userService.usernameOverlap((String) map.get("id"));     //중복 회원검사
        Map<String, Object> jsonMap = new HashMap<>();
        if (overlapEmail == true) {
            jsonMap.put("success","emailErr");                               //Json의 success key에 emailErr Value담기, 현재 입력된 아이디 값이 중복된 것을 알려줄 수 잇음
        } else if (overlapEmail == false) {
            jsonMap.put("success","success");                                           //Json의 success key에 success Value담기
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setID((String) map.get("id"));
            userInfo.setPW(bCryptPasswordEncoder.encode((String) map.get("pw")));        //패스워드 암호화
            userInfo.setNickName("이름모를 누군가"/*(String) map.get("username")*/);
            userInfo.setAuthority("ROLE_USER");
            userService.join(userInfo);
            userService.Send_Email(userInfo.getID());                                   //이메일 인증 함수 호출
        }
        return ResponseEntity.ok(jsonMap);                          //success여부 프론트에 송신
    }

    //회원가입 후 이메일 인증이 성공했는지를 반환하는 매핑
    @ResponseBody
    @PostMapping("/api/register/email")
    public ResponseEntity<?> ConfirmationEmail(@RequestBody HashMap<String,Object> map) {
        Map<String, Object> jsonMap = new HashMap<>();
        Optional<UserInfo> userinfo1 = userService.Get_UserInfo(username);
        if (userinfo1.get().getCheck_Email().equals((String)map.get("authNum"))) {
            jsonMap.put("success",true);            //인증번호가 동일하면 Json의 key에 true Value를 담음
        } else if (!userinfo1.get().getCheck_Email().equals((String)map.get("authNum"))) {
            jsonMap.put("success", false);          //인증번호가 동일하면 Json의 key에 true Value를 담음
        }
        return ResponseEntity.ok(jsonMap);          //성공여부를 프론트에 송신
    }
}