package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Page<Plan> findByUserInfo_UserId(Long UserId, Pageable pageable);

    Plan findByPlanId(Long plan_id);

    Plan save(Plan plan);

}