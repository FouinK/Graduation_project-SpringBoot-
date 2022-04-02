package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Entity.KakaoGeoRes;
import com.example.VivaLaTrip.Form.MapData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class MapController {

    @PostMapping
    public void mapRequest(@RequestBody MapData[] mapData) {
        for (int i = 0; i < mapData.length; i++) {
            log.info("JSOM 값 확인" + mapData[i]);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @GetMapping("/kakao")
    public @ResponseBody Map<String, Object> KakaoApiTest(Model model,@RequestParam("keyword") String eksdj) throws UnirestException, UnsupportedEncodingException, JsonProcessingException {
        String APIKey = "KakaoAK c8e5977da7751a0964896be7f0ac0701";

//        String address = "성결대";
        HashMap<String, Object> map = new HashMap<>(); //결과를 담을 map
        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        String apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=1&query="
                + URLEncoder.encode(eksdj, "UTF-8");

        HttpResponse<JsonNode>[] response= new HttpResponse[3];

        response[0] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=2&query="
                + URLEncoder.encode(eksdj, "UTF-8");

        response[1] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=3&query="
                + URLEncoder.encode(eksdj, "UTF-8");

        response[2] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        KakaoGeoRes bodyJson[] = new KakaoGeoRes[3];

        bodyJson[0] = objectMapper.readValue(response[0].getBody().toString(), KakaoGeoRes.class);
        bodyJson[1] = objectMapper.readValue(response[1].getBody().toString(), KakaoGeoRes.class);
        bodyJson[2] = objectMapper.readValue(response[2].getBody().toString(), KakaoGeoRes.class);

        map = objectMapper.convertValue(bodyJson[0], HashMap.class);
        map1 = objectMapper.convertValue(bodyJson[1], HashMap.class);
        map2 = objectMapper.convertValue(bodyJson[2], HashMap.class);

        map.putAll(map1);
        map.putAll(map2);

        log.info("맵값 확인 : "+map.toString());

        return (Map<String,Object>) map;
    }
}