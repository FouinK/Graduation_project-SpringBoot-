package com.example.VivaLaTrip.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class PubTransService {
    public void getPubTransPath() throws UnirestException {

        //System.out.println(arrayList.get(0).get("place_name")+"에서 " +arrayList.get(1).get("place_name"));
        String sX = "127.09806349478795";
        String sY = "37.51131985755065";

        String eX = "126.92766444856224";
        String eY = "37.38030121417301";

        String Apikey = "Pdk5AeqchgUHkzCZXjC3WGchUKGRIMf430BlyCwf18c";

        String url = "https://api.odsay.com/v1/api/searchPubTransPathT?SX="+sX+"&SY="+sY+"&EX="+eX+"&EY="+eY+"&apiKey=" +Apikey;

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Authorization", Apikey).asJson();

        System.out.println(response.getBody());
        JSONObject result = (JSONObject) response.getBody().getObject().get("result"); //json 객체만 뽑아냄
        System.out.println("결과출력 : "+result);
        JSONArray path = (JSONArray) result.get("path");
        JSONObject recommendedRoute = (JSONObject) path.get(0);
        JSONObject info = (JSONObject) recommendedRoute.get("info");
        String startStation = (String) info.get("firstStartStation");
        String endStation = (String) info.get("lastEndStation");
        Double totalDistance = (Double) info.get("totalDistance");
        int totalTime = (int) info.get("totalTime");

        System.out.println("출발지 : " +startStation+  "정류장"); //확인
        System.out.println("도착지 : " +endStation+    "정류장"); //확인
        System.out.println("총거리 : " +totalDistance+ "m");
        System.out.println("총시간 : " +totalTime+     "분");
    }
}