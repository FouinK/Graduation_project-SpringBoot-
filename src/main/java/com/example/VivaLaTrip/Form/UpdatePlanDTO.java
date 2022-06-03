package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Places;
import lombok.Data;

import java.util.List;

@Data
public class UpdatePlanDTO {
    String start_date;
    String end_date;
    String title;
    boolean isPublic;
    List<Places> places;
}
