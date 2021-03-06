package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Liked;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//Liked테이블을 직접적으로 CRUD를 처리할 수 있는 Repositoty
public interface LikedRepository extends JpaRepository<Liked, Long> {

    Liked findByPlan_PlanIdAndUserInfo_UserId(Long planId, Long userId);

    boolean existsByPlan_PlanIdAndUserInfo_UserId(Long planId, Long userId);

    List<Liked> findLikedsByPlan_PlanId(Long planId);

    boolean existsByPlan_PlanId(Long planId);
}
