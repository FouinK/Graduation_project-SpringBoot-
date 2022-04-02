package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.UserInfo;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Boolean existsByID(String ID);

    UserInfo save(UserInfo userInfo);

    Optional<UserInfo> findByID(String id);

    List<UserInfo> findAll();
}