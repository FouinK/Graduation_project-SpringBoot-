package com.example.VivaLaTrip.Form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MapData {
    private String address_name;
    private String category_group_code;
    private String category_group_name;
    private String category_name;
    private String distance;
    private String phone;
    private String place_name;
    private String place_url;
    private String road_address_name;
    private double x;
    private double y;
}