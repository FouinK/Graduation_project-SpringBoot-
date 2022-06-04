package com.example.VivaLaTrip.Entity;

import lombok.Data;

import javax.persistence.*;

//좋아요를 어떤 게시물에 누가 눌렀는지를 저장하는 Liked 컬럼들을 가지고있는 테이블 클래스
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