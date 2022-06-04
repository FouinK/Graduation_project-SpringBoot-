package com.example.VivaLaTrip.Entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

//Places 테이블의 컬럼들을 가지고 있는 클래스
@Entity
@Data
@Getter
@Setter
@Builder@NoArgsConstructor
@AllArgsConstructor
public class Places {

    @Id
    @Column(name = "place_id", nullable = false, unique = true)
    private String id;

    @Column(name = "place_name", nullable = false)
    private String place_name;

    @Column(name = "category_name", nullable = true)
    private String category_name;

    @Column(name = "y", nullable = false)
    private double y;

    @Column(name = "x", nullable = false)
    private double x;

    @Column(name = "phone")
    private String phone;

    @Column(name = "url")
    private String place_url;

    @Column(name = "address_name", nullable = false)
    private String addressName;

    @Column(name = "indoor", nullable = false)
    private String indoor;

    @Column(name = "stay")
    private int stay;

    @Column(name = "popularity")
    private int popularity;
}