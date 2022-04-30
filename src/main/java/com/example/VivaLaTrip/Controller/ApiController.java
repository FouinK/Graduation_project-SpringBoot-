package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Service.MapService;
import com.example.VivaLaTrip.Service.WeatherService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ApiController {

    WeatherService weatherService;
    MapService mapService;

    @Autowired
    public ApiController(WeatherService weatherService, MapService mapService) {
        this.weatherService = weatherService;
        this.mapService = mapService;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @PostMapping("/getData")
    public @ResponseBody
    ResponseEntity<?> KakaoApiTest(@RequestBody HashMap<String,Object> map) throws UnirestException, UnsupportedEncodingException, JsonProcessingException {

        String keyword = (String)map.get("keyword");        //일단 전체적인 지역 값 받아야함
//        String tempword = "충주시 호암동";

        return ResponseEntity.ok(mapService.MapParsingDB(keyword));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ResponseBody
    @GetMapping("/Weather")
    public Map<String, Object> WeatherAPI(@RequestParam("Date") int date) throws IOException, UnirestException {

        log.info("컨트롤러 date값 : " + date);

        Map<String, Object> map = weatherService.WeatherParsing(date);

        return map;
    }
}