package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDataUserRepository extends JpaRepository<UserInfo, Long>, UserRepository {

    //아이디로 회원정보 호출
    @Override
    Optional<UserInfo> findByID(String id);

    //회원 저장
    @Override
    UserInfo save(UserInfo userInfo);

    //전체 검색
    @Override
    List<UserInfo> findAll();

}
