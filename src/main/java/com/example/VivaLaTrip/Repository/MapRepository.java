package com.example.VivaLaTrip.Repository;

import com.example.VivaLaTrip.Entity.Places;

import java.util.List;

public interface MapRepository {

    List<Places> findByAddressContains(String word);

    Places save(Places places);

}