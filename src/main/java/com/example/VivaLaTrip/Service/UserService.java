package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Form.FailureUserInfo;
import com.example.VivaLaTrip.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    //서비스 생성자에 리포지토리 주입
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //회원 전체 정보 불러오기
    public List<UserInfo> view() {
        return userRepository.findAll();
    }

    //아이디 중복검사
    public void ValidateUser(UserInfo userInfo) {
        userRepository.findByID(userInfo.getID())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
    /* 회원가입 시, 유효성 체크 */
    @Transactional
    public Map<String, String> validateHandling(Errors errors) {
        Map<String, String> validatorResult = new HashMap<>();
        /* 유효성 검사에 실패한 필드 목록을 받음 */
        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = String.format("valid_%s", error.getField());
            validatorResult.put(validKeyName, error.getDefaultMessage());
        }
        return validatorResult;
    }

    //id 중복 검사
    public Boolean usernameOverlap(String username) {
        return userRepository.existsByID(username);
    }

    //데이터베이스에 새로운 회원 저장
    public String join(UserInfo userInfo) {
        ValidateUser(userInfo);
        userRepository.save(userInfo);
        return userInfo.getUsername();
    }

    //데이터베이스에 저장된 회원정보 가져오기
    public Optional<UserInfo> Get_UserInfo(String id) {
        Optional<UserInfo> userInfo = userRepository.findByID(id);
        return userInfo;
    }

    //스프링 시큐리티 로그인 시 회원정보 조회
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userRepository.findByID(id);

        //회원정보가 없다면 User객체에 key값들이 Null이 담긴 객체를 리턴
        if (!userInfo.isPresent()) {
            return new FailureUserInfo();
        }

        //회원이 존재한다면 스프링 서버에서 개인정보가 저장되지 않은(단순 ID값과 유저 역할) User객체를 전역으로 생성 (로그아웃 되거나 세션 값이 만료되기 전까지 유효)
        return new User(userInfo.get().getID(), userInfo.get().getPassword(), userInfo.get().getAuthorities());
    }

    //회원가입 시 필요한 Email 인증번호 난수 생성
    public String GenerateCertNumber() {
        int certNumLength = 6;
        Random random = new Random(System.currentTimeMillis());
        int range = (int) Math.pow(10, certNumLength);
        int trim = (int) Math.pow(10, certNumLength - 1);
        int result = random.nextInt(range) + trim;
        if (result > range) {
            result = result - trim;
        }
        return String.valueOf(result);
    }

    //회원가입 시 입력 받은 이메일로 인증번호를 송신
    public void Send_Email(String email) {
        String send_code = GenerateCertNumber();        //인증번호 난수 생성
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("Viva La Trip 인증 메일입니다.");
        simpleMailMessage.setText("인증번호 "+send_code+"를 입력해주세요.");

        Optional<UserInfo> userInfo = Get_UserInfo(email);
        userInfo.get().setCheck_Email(send_code);
        Update_Userinfo_Check_Email(userInfo);          //Userinfo 테이블 업데이트

        javaMailSender.send(simpleMailMessage);         //입력된 이메일에 직접 송신
    }

    //람다식으로 UserInfo check_email 업데이트
    public void Update_Userinfo_Check_Email(Optional<UserInfo> userinfo) {
        userinfo.ifPresent(selectUserInfo->{
            selectUserInfo.setCheck_Email(userinfo.get().getCheck_Email());
//            selectUserInfo.setAuthority("ROLE_TEMPORARY_USER");
            userRepository.save(selectUserInfo);
        });
    }

    //람다식으로 UserInfo ROLE 업데이트
    public void Update_Userinfo_ROLE_USER(Optional<UserInfo> userinfo) {
        userinfo.ifPresent(selectUserInfo->{
            selectUserInfo.setAuthority("ROLE_USER");
            userRepository.save(selectUserInfo);
        });
    }
}