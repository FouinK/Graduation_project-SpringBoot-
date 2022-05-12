package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.PlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PlanDetailRepository extends JpaRepository<PlanDetail, Long> {



    PlanDetail save(PlanDetail planDetail);
}

