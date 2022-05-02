package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapRepository extends JpaRepository<Places,Long>{


//    @Override
    List<Places> findByAddressContains(String word);

//    @Override
    Places save(Places places);

}