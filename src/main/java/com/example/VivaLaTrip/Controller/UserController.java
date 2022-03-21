package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.UserForm;
import com.example.VivaLaTrip.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //시큐리티 회원가입(해시 함수 적용된 PW암호화)
    @PostMapping("/sign_uppro")
    public String sign_uppro(UserForm userForm) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserInfo userInfo = new UserInfo();
        userInfo.setID(userForm.getUsername());
        userInfo.setPW(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userInfo.setUserName(userForm.getUserName());
        userInfo.setLiked(userForm.getLiked());
        userInfo.setAuthority("ROLE_USER");
        userService.join(userInfo);
        return "login";
    }

    @GetMapping("/api/overlap/usernameRegister")
    public ResponseEntity<?> mapReturn(String username) {
        HashMap<String, Object> map = userService.usernameOverlap(username);
        return ResponseEntity.ok(map);
    }

}


