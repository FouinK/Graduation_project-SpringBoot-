package com.example.VivaLaTrip.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity //Plan 클래스가 MYSQL 테이블이 생성
public class Plan extends BaseTimeEntity{

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    @Column(name = "plan_id")
    private Long planId;

    @OneToOne(mappedBy = "plan")
    //mappedBy 연관관계의 주인이 아니다. DB에 칼럼을 만들지마라. PublicPlan의 필드이름을 적어줌 걔가 외래키
    private PublicPlan publicPlan;

    @ManyToOne(fetch = FetchType.EAGER)//연관관계 Plan이 Many, UserInfo가 One ==한 명의 user는 여러 Plan생성가능
    //@JoinColumn(name="userId") //실제로 db에 만들어질 이름
    private UserInfo userInfo;
    //DB는 오브젝트 저장 불가. FK, 자바는 오브젝트 저장할 수 있다. => 충돌발생


    @Column(name = "is_public",nullable = false)
    private boolean is_public;

    @Column(name = "total_count",nullable = false)
    private int total_count;

    @Column(name = "start_date",nullable = false)
    private String start_date;

    @Column(name = "end_date",nullable = false)
    private String end_date;

    @Column(name = "from_planId",nullable = false)
    private Long fromPlanId;

    @Column(name = "comment",nullable = false)
    private String comment;
}
