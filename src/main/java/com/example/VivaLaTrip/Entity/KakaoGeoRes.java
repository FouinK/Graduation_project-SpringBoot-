package com.example.VivaLaTrip.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown =true)
@Getter
@Setter
public class KakaoGeoRes {
    private List<Documents> documents;
//    private HashMap<String, Object> meta;
}