package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Plan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanRequestDto {
    private Long plan_id;
    private String plan;
    //private String ; userid userinfo에서 갖고와야하고
    //private UserInfo userInfo;

    private boolean is_public;
    private int total_count;
    private String start_date;
    private String end_date;

    /*@Builder
    public PlanRequestDto(Long plan_id,UserInfo userInfo, String plan, boolean is_public, int total_count, String start_date, String end_date) {
        this.plan_id = plan_id;
        this.plan = plan;
        this.userInfo=userInfo;
        this.is_public = is_public;
        this.total_count = total_count;
        this.start_date = start_date;
        this.end_date = end_date;
    }*/

    public Plan toEntity(){
        return Plan.builder()
                .planId(plan_id)
                .is_public(is_public)
                .total_count(total_count)
                .start_date(start_date)
                .end_date(end_date)
                .build();
    }
}

