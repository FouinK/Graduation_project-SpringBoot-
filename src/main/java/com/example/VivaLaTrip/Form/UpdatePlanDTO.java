package com.example.VivaLaTrip.Form;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePlanDTO {
    String start_date;
    String end_date;
    String title;
    int liked;
    boolean isPublic;
    List<PlanDetailDTO> places;
}
