package com.example.VivaLaTrip.OpenWeatherDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ShortOpenWeatherDto {
    private Response response;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class Response {
    private Body body;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class Body {
    private Items items;

}
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
class Items {
    private List<Item> item;
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
class Item {
    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private String nx;
    private String ny;
}
