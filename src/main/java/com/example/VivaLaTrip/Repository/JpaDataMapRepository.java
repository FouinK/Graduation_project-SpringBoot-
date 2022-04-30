package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDataMapRepository extends JpaRepository<Places, Long>, MapRepository {


    @Override
    List<Places> findByAddressContains(String word);

    @Override
    Places save(Places places);

}