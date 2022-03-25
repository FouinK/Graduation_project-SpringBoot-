package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Plan;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PlanUpdateRequestDto {
    private String title;
    private String content;
    private String place_list;

    @Builder
    public PlanUpdateRequestDto(String title,String content,String place_list){
        this.title=title;
        this.content=content;
        this.place_list=place_list;
    }
}
