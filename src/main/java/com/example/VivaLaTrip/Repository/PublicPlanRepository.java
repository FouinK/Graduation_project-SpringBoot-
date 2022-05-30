package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.PublicPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublicPlanRepository extends JpaRepository<PublicPlan, Long> {

    Page<PublicPlan> findAll(Pageable pageable);

    PublicPlan findByPlanId(Long plan_id);

    PublicPlan save(PublicPlan publicPlan);

    boolean existsByPlanId(Long plan_id);

    void deleteByPlanPlanId(Long plan_id);

}
