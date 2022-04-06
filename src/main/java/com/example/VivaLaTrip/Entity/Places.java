package com.example.VivaLaTrip.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Getter
@Setter
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

    @Column(name = "phone")
    private String phone;

    @Column(name = "url")
    private String url;

    @Column(name = "address_name", nullable = false)
    private String address_name;

    @Column(name = "indoor", nullable = false)
    private String indoor;
}