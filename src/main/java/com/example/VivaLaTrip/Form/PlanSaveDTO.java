package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Places;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanSaveDTO {
    private boolean isPublic;
    private String title;
    private List<Places> checkedPlace;
    private String start_date;
    private String end_date;

}