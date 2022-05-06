package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PublicPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicPlanRepository extends JpaRepository<PublicPlan, Long> {

    List<PublicPlan> findAll();

    PublicPlan findByPlanId(Long plan_id);

    PublicPlan save(PublicPlan publicPlan);

}
