package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Places {

    @Id
    @Column(name = "place_id", nullable = false, unique=true)
    private String place_id;

    @Column(name = "place_name", nullable = false)
    private String place_name;

    @Column(name = "category_name", nullable = false)
    private String category_name;

    @Column(name = "y", nullable = false)
    private String y;

    @Column(name = "x", nullable = false)
    private String x;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "address_name", nullable = false)
    private String address_name;

}