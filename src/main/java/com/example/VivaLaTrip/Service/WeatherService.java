package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.OpenWeatherDto.OpenWeather;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WeatherService {

    //날짜 허용 범위 검사
    public int DateGap(int date) {

        log.info("DateGap date값 : "+date);

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatedNow = now.format(formatter);

        int Today = Integer.parseInt(formatedNow);
        int gap = date - Today;
        if (gap > 10) {
            throw new IllegalStateException("날짜 차이가 너무 남");
        }

        return date;
    }

    //날씨 파싱
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ResponseBody
    public Map<String, Object> WeatherParsing(int date) throws UnirestException, JsonProcessingException {

        log.info("서비스 date값 : "+date);

        int truedate = DateGap(date);

        String ServiceKey = "c3EMGmp9ih88slfSXrMpZCf9LDM7SRCD7bP1L9Yiw9HfRj5S8V8EPpsmFYDG9jjK%2Fy%2F9OR1JyJ9EacBvsshu0w%3D%3D";

        String apiURL = "http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst?serviceKey="+ ServiceKey
                +"&pageNo=1"
                +"&numOfRows=10"
                +"&dataType=JSON"
                +"&regId=11B00000"
                +"&tmFc="+truedate+"0600";

        HttpResponse<JsonNode> response= Unirest.get(apiURL)
                .asJson();


        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        OpenWeather openWeather = objectMapper.readValue(response.getBody().toString(), OpenWeather.class);

        HashMap<String, Object> map = objectMapper.convertValue(openWeather, HashMap.class);
//        HashMap<String, Object> map1 = objectMapper.readValue(response.getBody().toString(), HashMap.class);

        log.info("맵값 확인 : "+map.toString());
//        log.info("맵값 확인 : "+map1.toString());

        return (Map<String,Object>) map;
    }


}
