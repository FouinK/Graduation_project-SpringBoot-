package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.UserInfo;
import com.example.VivaLaTrip.Repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
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
