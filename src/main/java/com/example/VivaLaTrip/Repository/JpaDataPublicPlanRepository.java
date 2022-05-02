package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Public_Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDataPublicPlanRepository extends JpaRepository<Public_Plan,Long>, PublicPlanRepository {
    @Override
    List<Public_Plan> findAll();
    @Override
    Public_Plan save(Public_Plan public_plan);
}


