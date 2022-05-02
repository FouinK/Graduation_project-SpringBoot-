package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.PublicPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPlanRepository extends JpaRepository<PublicPlan, Long> {

    /*List<PublicPlan> findAll();
    PublicPlan save(PublicPlan public_plan);*/
}
