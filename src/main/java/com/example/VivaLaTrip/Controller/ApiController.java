package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.Places;
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
import java.util.List;
import java.util.Map;

/*기존 Places호출 시 KAKAO API호출을 통하여 Places개수 45개를 호출 담당했던 Controller이지만 Places의 값을 자체 데이터 베이스에 저장함으로써
/자체 데이터베이스에 있는 Places값 호출을 위한 Controller로 변환*/
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

    //Places검색 요청을 받는 Mapping url
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @PostMapping("/getData")
    public @ResponseBody
    ResponseEntity<List<Places>> KakaoApiTest(@RequestBody HashMap<String,Object> map) {

        String keyword = (String)map.get("keyword"); //장소에 대한 정보를 받는 keyword 값(자체 데이터 베이스의 각 Places가 가지고있는 주소에 포함 된 문자열을 검색 할 키워드)

        return ResponseEntity.ok(mapService.MapParsingDB(keyword)); //데이터 베이스에서 찾아온 100개의 Places를 프론트로 송신
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @ResponseBody
    @GetMapping("/Weather")
    public Map<String, Object> WeatherAPI(@RequestParam("Date") int date) throws IOException, UnirestException {

        log.info("컨트롤러 date값 : " + date);

//        Map<String, Object> map = weatherService.LongWeatherParsing(date);
        Map<String, Object> map = weatherService.ShortWeatherParsing(date);

        return map;
    }
}