package com.example.VivaLaTrip.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanListDTO {
    private String userId;
    private String start_date;
    private String end_date;
    private String plan_id;
    private String title;
    private int place_num;
    private int liked;
}

