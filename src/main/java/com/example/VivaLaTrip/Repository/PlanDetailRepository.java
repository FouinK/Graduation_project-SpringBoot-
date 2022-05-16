package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {
    PlanDetail save(PlanDetail planDetail);
}
