package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class PublicPlan implements Serializable {

    @Id
    private Long plan_id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @Column(name = "comment",nullable = false)
    private String comment;

    @Column(name = "like_count")
    private int like_count;
}
