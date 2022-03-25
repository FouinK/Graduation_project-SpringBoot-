package com.example.VivaLaTrip.Form;

import com.example.VivaLaTrip.Entity.Plan;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanSaveRequestDto {
    private String title;
    private String content;
    private String author;
    private String place_list;

    @Builder
    public PlanSaveRequestDto(String title, String content, String author, String place_list){
        this.title=title;
        this.author=author;
        this.content=content;
        this.place_list=place_list;
    }

    public Plan toEntity(){
        return Plan.builder()
                .title(title)
                .content(content)
                .author(author)
                .place_list(place_list)
                .build();
    }
}