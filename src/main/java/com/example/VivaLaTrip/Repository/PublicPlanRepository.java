package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import com.example.VivaLaTrip.Entity.Public_Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicPlanRepository {

    List<Public_Plan> findAll();
    Public_Plan save(Public_Plan public_plan);

    Public_Plan getById(Long id);
}
