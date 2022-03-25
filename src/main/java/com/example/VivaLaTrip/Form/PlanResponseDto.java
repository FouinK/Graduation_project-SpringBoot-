package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Plan;
import lombok.Getter;

@Getter
public class PlanResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private String place_list;

    public PlanResponseDto(Plan entity){
        this.id=entity.getId();
        this.title=entity.getTitle();
        this.content=entity.getContent();
        this.author=entity.getAuthor();
        this.place_list=entity.getPlace_list();
    }
}
