package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Plan;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PlanListResponseDto {

    private Long id;
    private String title;
    private String author;
    private String place_list;
    private LocalDateTime modifiedDate;


    public PlanListResponseDto(Plan entity){
        this.id= entity.getId();
        this.title= entity.getTitle();
        this.author= entity.getAuthor();
        this.place_list=entity.getPlace_list();
        this.modifiedDate=entity.getModifiedDate();
    }
}
