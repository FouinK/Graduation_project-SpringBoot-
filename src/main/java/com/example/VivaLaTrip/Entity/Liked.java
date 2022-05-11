package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Liked extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;

    @ManyToOne
    private UserInfo userInfo;

    @ManyToOne
    private Plan plan;

}