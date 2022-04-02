package com.example.VivaLaTrip.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown =true)
class Documents {
//    private String category_group_code;
//    private String category_group_name;
//    private String category_name;
//    private String distance;
//    private String id;
//    private String phone;
    private String place_name;
//    private String place_url;
    private Double x;
    private Double y;
//    private String address_name;
//    private String road_address_name;
}