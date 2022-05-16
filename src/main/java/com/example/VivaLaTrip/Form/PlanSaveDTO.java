package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Places;
import lombok.Data;
import java.util.List;

@Data
public class PlanSaveDTO {
    private boolean is_public;
    private String title;
    private List<Places> checkedPlace;
    private String start_date;
    private String end_date;
}
