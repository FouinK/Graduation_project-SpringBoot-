package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Liked;
import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    Liked findByPlan_PlanIdAndUserInfo_UserId(Long planId, Long userId);

    boolean existsByPlan_PlanIdAndUserInfo_UserId(Long planId, Long userId);
}
