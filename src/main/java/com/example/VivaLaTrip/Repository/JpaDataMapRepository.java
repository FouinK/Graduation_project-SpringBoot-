package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDataMapRepository extends JpaRepository<Places, Long>, MapRepository {
    @Override
    Places save(Places places);

}
