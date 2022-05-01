package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Plan_temp extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plan_id;

    @Column(name = "user_id",nullable = false)
    private int user_id;

    @Column(name = "is_public",nullable = false)
    private boolean is_public;

    @Column(name = "total_count",nullable = false)
    private int total_count;

    @Column(name = "start_date",nullable = false)
    private String start_date;

    @Column(name = "end_date",nullable = false)
    private String end_date;


}
