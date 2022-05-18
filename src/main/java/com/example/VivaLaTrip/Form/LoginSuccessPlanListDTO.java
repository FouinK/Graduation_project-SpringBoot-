package com.example.VivaLaTrip.Form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor
@AllArgsConstructor
public class LoginSuccessPlanListDTO {
    private boolean loginSuccess;
    private int totalPage;
    private List<PlanListDTO> myPlans;
}