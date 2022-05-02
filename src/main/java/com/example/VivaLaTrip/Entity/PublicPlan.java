package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
@Data
public class PublicPlan implements Serializable {

    @Id
    @ManyToOne
    private PlanTemp planTemp;

    @Column(name = "comment",nullable = false)
    private String comment;

    @Column(name = "like_count")
    private int like_count;

}
