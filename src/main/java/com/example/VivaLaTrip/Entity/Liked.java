package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Liked extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    @Column(name = "user_id",nullable = false)
    private int user_id;

    @Column(name = "plan_id",nullable = false)
    private String plan_id;

}
