package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    @Query("SELECT p FROM Plan p ORDER BY p.id DESC")
    List<Plan> findAllDesc();

}