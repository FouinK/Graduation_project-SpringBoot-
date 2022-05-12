package com.example.VivaLaTrip.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailDTO {
    private String id;
    private int day;
    private String place_id;
}
