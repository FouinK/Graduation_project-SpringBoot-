package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<Places, Long> {

    List<Places> findAll();

    List<Places> findByAddressNameContains(String word, Sort sort);

    List<Places> findByXBetweenAndYBetween(double x_min, double x_max, double Y_min, double Y_max, Sort sort);

    Places findById(String id);

    Places save(Places places);
}