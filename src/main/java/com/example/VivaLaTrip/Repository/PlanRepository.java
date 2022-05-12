package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PlanDetail;
import com.example.VivaLaTrip.Entity.PublicPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    /*
        List<Plan> findAll();
    */
    List<Plan> findAllByUserInfo_UserId(Long UserId);

    Plan findByPlanId(Long plan_id);

    Plan save(Plan plan);


    List<Plan> findAllByPlanId(Long planid);


}
