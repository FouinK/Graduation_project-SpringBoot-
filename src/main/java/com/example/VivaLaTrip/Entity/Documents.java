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
    private String x;
    private String y;
    private String phone;
    private String place_url;
    private String category_name;
    private String address_name;
    private String id;
}