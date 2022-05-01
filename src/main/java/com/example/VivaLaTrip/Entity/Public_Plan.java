package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Public_Plan {

    @Id
    @Column(name = "plan_id",nullable = false)
    private int plan_id;

    @Column(name = "comment",nullable = false)
    private String comment;

    @Column(name = "like_count")
    private int like_count;

}
