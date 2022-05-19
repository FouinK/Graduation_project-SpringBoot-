package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PublicPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicPlanRepository extends JpaRepository<PublicPlan, Long> {

    Page<PublicPlan> findAll(Pageable pageable);

    PublicPlan findByPlanId(Long plan_id);

    PublicPlan save(PublicPlan publicPlan);

}
