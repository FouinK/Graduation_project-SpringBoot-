package com.example.VivaLaTrip.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Plan extends BaseTimeEntity {
    @Id//테이블 pk필드
    @GeneratedValue(strategy = GenerationType.IDENTITY)//pk생성 규칙
    private Long id;

    @Column(length=500,nullable = false)//테이블 컬럼
    private String title;

    @Column(length = 500,nullable = false)
    private String content;

    private String author;

    @Column(length = 500,nullable = false)
    private String place_list;

    @Builder//빌더 패턴 클래스 생성
    public Plan(String title, String content, String author, String place_list){
        this.title=title;
        this.content=content;
        this.author=author;
        this.place_list=place_list;
    }

    public void update(String title,String content,String place_list){
        this.title=title;
        this.content=content;
        this.place_list=place_list;
    }
}