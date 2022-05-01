package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class PlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    @Column(name = "plan_id",nullable = false)
    private int plan_id;

    @Column(name = "days",nullable = false)
    private int days;

    @Column(name = "place_id",length = 500,nullable = false)
    private String place_id;

}
