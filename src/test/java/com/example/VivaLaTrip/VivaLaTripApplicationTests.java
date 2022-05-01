package com.example.VivaLaTrip;

import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.OpenWeatherDto.OpenWeather;

import com.example.VivaLaTrip.Repository.MapRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Transactional
public class VivaLaTripApplicationTests {

    @Test
    String test() {
        return "test complete";
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Test
    @ResponseBody
    void TestAPI() throws IOException, UnirestException {


        String apiURL = "https://api.openweathermap.org/data/2.5/onecall?lat=127.126394371247&lon=37.4199574744224&exclude=daily&appid=92c8a772db575c631426e339f97db124";


        HttpResponse<JsonNode> response = Unirest.get(apiURL)
                .asJson();

//        JSONParser jsonParser = new JSONParser();
//        JSONObject jsonObject = (JSONObject) jsonParser.parse();

        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        OpenWeather openWeather = objectMapper.readValue(response.getBody().toString(), OpenWeather.class);

//
        HashMap<String, Object> map = objectMapper.readValue(openWeather.toString(), HashMap.class);

        HashMap<String, Object> map1 = objectMapper.readValue(response.getBody().toString(), HashMap.class);


        System.out.println("테스트 API(날씨) 맵 값 확인 : "+map.toString());

    }

    @Test
    public void search() throws UnirestException, JsonProcessingException {

    }
/*
    @Test
    public void MapParsingDB() {
        String word = "강원";
        MapRepository mapRepository = null;
        List<Places> bodyJson = mapRepository.findByAddressContains(word);
        System.out.println("데베에서 뽑아온 Places값 :"+bodyJson.toString());
    }*/

}