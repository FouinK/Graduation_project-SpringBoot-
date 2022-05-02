package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    @ManyToOne
    private Plan plan;

    @Column(name = "days",nullable = false)
    private int days;

    @Column(name = "place_id",length = 500,nullable = false)
    private String place_id;

}
