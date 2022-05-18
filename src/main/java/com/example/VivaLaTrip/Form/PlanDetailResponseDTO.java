package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Places;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailResponseDTO {
    Long userId;
    String start_date;
    String end_date;
    Long plan_id;
    String title;
    int place_num;
    int liked;
    List<PlanDetailDTO> places;
}
