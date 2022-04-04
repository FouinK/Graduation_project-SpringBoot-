package com.example.VivaLaTrip.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown =true)
public class Documents {
    private String place_name;
    private Double x;
    private Double y;
}