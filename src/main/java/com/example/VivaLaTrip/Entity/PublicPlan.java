package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class PublicPlan implements Serializable {

    @Id@Column(name = "plan_id")
    private Long planId;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "plan_id")
    private Plan plan;


    @Column(name = "like_count")
    private int like_count;
}
