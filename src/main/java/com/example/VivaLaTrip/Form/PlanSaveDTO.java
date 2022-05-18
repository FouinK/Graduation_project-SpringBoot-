package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Places;
import lombok.Data;
import java.util.List;

@Data
public class PlanSaveDTO {
    private boolean isPublic;
    private String title;
    private List<Places> checkedPlace;
    private String start_date;
    private String end_date;
}
