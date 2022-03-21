package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public class UserService implements UserDetailsService {

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
    public HashMap<String, Object> usernameOverlap(String username) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("result", userRepository.existsByID(username));
        return map;
    }

    //데이터베이스에 새로운 회원 저장
    public String join(UserInfo userInfo) {
        ValidateUser(userInfo);
        userRepository.save(userInfo);
        return userInfo.getUsername();
    }

    //회원정보 불러오기 (000님 안녕하세요. ID말고 이름 출력 위해서)
    public Optional<UserInfo> Get_UserInfo(String id) {
        Optional<UserInfo> userInfo = userRepository.findByID(id);
        return userInfo;
    }

    //스프링 시큐리티 로그인 시 아이디 검사
    @Override
    public UserDetails loadUserByUsername(String insertid) throws UsernameNotFoundException {
        Optional<UserInfo> userInfo = userRepository.findByID(insertid);
        return new User(userInfo.get().getID(),userInfo.get().getPassword(),userInfo.get().getAuthorities());
    }
}
