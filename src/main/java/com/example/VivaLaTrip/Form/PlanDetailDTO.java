package com.example.VivaLaTrip.Form;
import lombok.*;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class PlanDetailDTO {
    String id;
    String place_name;
    double x;
    double y;
    boolean checked;
    int day;
}