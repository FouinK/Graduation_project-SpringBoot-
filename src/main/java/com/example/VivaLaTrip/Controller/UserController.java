package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.UserForm;
import com.example.VivaLaTrip.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
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
            log.info("컨트롤러에서 받은 유저폼의 아이디"+userForm.getUsername());
            log.info("컨트롤러에서 받은 유저폼의 아이디"+userForm.getPassword());
            log.info("컨트롤러에서 받은 유저폼의 아이디"+userForm.getUserName_());
            log.info("컨트롤러에서 받은 유저폼의 아이디"+userForm.getLiked());
        /* 회원가입 페이지로 다시 리턴 */
        return "/sign_up";
    }
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserInfo userInfo = new UserInfo();
        userInfo.setID(userForm.getUsername());
        userInfo.setPW(bCryptPasswordEncoder.encode(userForm.getPassword()));
        userInfo.setUserName(userForm.getUserName_());
        userInfo.setLiked(userForm.getLiked());
        userInfo.setAuthority("ROLE_USER");
        userService.join(userInfo);
        return "login";
    }

    //회원가입시 아이디 중복체크
    @GetMapping("/api/overlap/usernameRegister")
    public ResponseEntity<?> mapReturn(String username) {
        HashMap<String, Object> map = userService.usernameOverlap(username);
        return ResponseEntity.ok(map);
    }

}


