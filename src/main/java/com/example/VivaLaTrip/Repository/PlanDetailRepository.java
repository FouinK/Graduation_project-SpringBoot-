package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    PlanDetail save(PlanDetail planDetail);
    List<PlanDetail> findAllByPlan_PlanId(Long planId);
}
