package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    Plan findByPlanId(Long plan_id);

}
