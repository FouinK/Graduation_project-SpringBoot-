package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.KakaoGeoRes;
import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Repository.MapRepository;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class MapService {

    private MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ResponseBody
    public Map<String, Object> MapParsing(String word) throws UnsupportedEncodingException, UnirestException, JsonProcessingException {

//        log.info("서비스  word값 : "+word);

        String APIKey = "KakaoAK c8e5977da7751a0964896be7f0ac0701";

        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        String apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=1&query="
                + URLEncoder.encode(word, "UTF-8");

        HttpResponse<JsonNode>[] response= new HttpResponse[3];

        response[0] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=2&query="
                + URLEncoder.encode(word, "UTF-8");

        response[1] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=3&query="
                + URLEncoder.encode(word, "UTF-8");

        response[2] = Unirest.get(apiURL)
                .header("Authorization", APIKey)
                .asJson();

        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);


        KakaoGeoRes bodyJson[] = new KakaoGeoRes[3];

        bodyJson[0] = objectMapper.readValue(response[0].getBody().toString(), KakaoGeoRes.class);
        bodyJson[1] = objectMapper.readValue(response[1].getBody().toString(), KakaoGeoRes.class);
        bodyJson[2] = objectMapper.readValue(response[2].getBody().toString(), KakaoGeoRes.class);

        HashMap<String, Object> map = objectMapper.convertValue(bodyJson[0], HashMap.class);
        HashMap<String, Object> map4 = new HashMap<String, Object>();

//        map1 = objectMapper.convertValue(bodyJson[1], HashMap.class);
//        map2 = objectMapper.convertValue(bodyJson[2], HashMap.class);

        map4.put("x", bodyJson[0].getDocuments().get(1).getX());
        map4.put("y",bodyJson[0].getDocuments().get(1).getY());

//        log.info("서비스에서 documents만 파싱한 값 : "+map4.toString());

//        List<KakaoGeoRes> list = new ArrayList<KakaoGeoRes>();

//        log.info("맵값 확인 : "+map.toString());

        Save_Places(bodyJson);

        return (HashMap<String, Object>) map4;
    }

    public void Save_Places(KakaoGeoRes bodyJson[]) {
//        log.info("세이브 메소드에서 받은 bodyJson id값 : " + bodyJson[0].getDocuments().get(0).getId());


        Places[] places = new Places[15];
        for (int i = 0; i < 15; i++) {
            places[i] = new Places();
        }
//        places[0].setPlace_id(bodyJson[0].getDocuments().get(0).getId());
//        log.info("Places배열 정상 작동"+places[0].getPlace_id());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 15; j++) {
                places[j].setPlace_id(bodyJson[i].getDocuments().get(j).getId());
                places[j].setPlace_name(bodyJson[i].getDocuments().get(j).getPlace_name());
                places[j].setAddress_name(bodyJson[i].getDocuments().get(j).getAddress_name());
                places[j].setPhone(bodyJson[i].getDocuments().get(j).getPhone());
                places[j].setUrl(bodyJson[i].getDocuments().get(j).getPlace_url());
                places[j].setX(bodyJson[i].getDocuments().get(j).getX());
                places[j].setY(bodyJson[i].getDocuments().get(j).getY());
                places[j].setCategory_name(bodyJson[i].getDocuments().get(j).getCategory_name());
                places[j].setIndoor("0");
                mapRepository.save(places[j]);
            }
        }
//        log.info("for문 적상 작동");
//        log.info("맵 리포지토리 정상 작동");
    }

/*    public Map<String,Object> getByKeyword(String keyword) throws JsonProcessingException, UnirestException {
        String APIKey = "KakaoAK 987e4dd94656cea7557c313bec550aee";
        HashMap<String, Object> map = new HashMap<>(); //결과를 담을 map

        String apiURL = "https://dapi.kakao.com/v2/local/search/keyword.json?query="
                + URLEncoder.encode(keyword, UTF_8);

        HttpResponse<JsonNode> response = Unirest.get(apiURL)
                .header("Authorization", APIKey).asJson();


        JSONObject jsonObject = response.getBody().getObject(); //json 객체만 뽑아냄

        System.out.println(jsonObject); //확인

        JSONArray jsonArray = (JSONArray) jsonObject.get("documents"); //documents만 뽑아냄

        JSONObject tmp = (JSONObject)jsonArray.get(0);
        String place_name = (String)tmp.get("place_name");
        String x = (String)tmp.get("x");
        String y = (String)tmp.get("y");

        map.put("place_name",place_name);
        map.put("x",x);
        map.put("y",y);


        *//*ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        map = objectMapper.readValue(response.getBody().toString(), HashMap.class);*//*

        return (Map<String, Object>) map;
    }*/

}