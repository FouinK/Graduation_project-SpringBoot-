package com.example.VivaLaTrip.OpenWeatherDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeather {
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
    private String wf3Am;
    private String wf3Pm;
    private String wf4Am;
    private String wf4Pm;
    private String wf5Am;
    private String wf5Pm;
    private String wf6Am;
    private String wf6Pm;
    private String wf7Am;
    private String wf7Pm;
    private String wf8;
    private String wf9;
    private String wf10;
}