package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Plan extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long plan_id;

    @OneToOne(mappedBy = "plan")
    private PublicPlan publicPlan;

    @ManyToOne
    private UserInfo userInfo;

    @Column(name = "is_public",nullable = false)
    private boolean is_public;

    @Column(name = "total_count",nullable = false)
    private int total_count;

    @Column(name = "start_date",nullable = false)
    private String start_date;

    @Column(name = "end_date",nullable = false)
    private String end_date;


}
