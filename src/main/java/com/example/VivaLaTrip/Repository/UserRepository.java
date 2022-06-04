package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//UserInfo테이블을 직접적으로 CRUD를 처리할 수 있는 Repositoty
public interface UserRepository extends JpaRepository<UserInfo, Long> {

    //아이디로 회원 존재 여부 리턴
    Boolean existsByID(String ID);

    //아이디로 회원정보 호출
    Optional<UserInfo> findByID(String id);

    //회원 저장
    UserInfo save(UserInfo userInfo);

    //회원 전체 검색
    List<UserInfo> findAll();

}

