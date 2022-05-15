package com.example.VivaLaTrip.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Comparator;

@Entity
@Data
@Getter
@Setter
public class Places {

    @Id
    @Column(name = "place_id", nullable = false, unique = true)
    private String id;

    @Column(name = "place_name", nullable = false)
    private String place_name;

    @Column(name = "category_name", nullable = true)
    private String category_name;

    @Column(name = "y", nullable = false)
    private String y;

    @Column(name = "x", nullable = false)
    private String x;

    @Column(name = "phone")
    private String phone;

    @Column(name = "url")
    private String place_url;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "indoor", nullable = false)
    private String indoor;

/*
    @Column(name = "checked", nullable = false)
    private boolean checked;
*/

    @Column(name = "stay")
    private int stay;

    @Column(name = "popularity")
    private int popularity;
}