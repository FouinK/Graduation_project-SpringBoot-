package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapRepository extends JpaRepository<Places,Long>{

    List<Places> findAll();

    List<Places> findByAddressNameContains(String word, Sort sort);

    Places save(Places places);
}