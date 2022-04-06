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
    public void getPubTransPath(ArrayList<Map> arrayList) throws UnirestException {

        System.out.println(arrayList.get(0).get("place_name")+"에서 " +arrayList.get(1).get("place_name"));
        String sX = (String) arrayList.get(0).get("x");
        String sY = (String) arrayList.get(0).get("y");

        String eX = (String) arrayList.get(1).get("x");
        String eY = (String) arrayList.get(1).get("y");

        String Apikey = "Pdk5AeqchgUHkzCZXjC3WGchUKGRIMf430BlyCwf18c";

        String url = "https://api.odsay.com/v1/api/searchPubTransPathT?SX="+sX+"&SY="+sY+"&EX="+eX+"&EY="+eY+"&apiKey=" +Apikey;

        HttpResponse<JsonNode> response = Unirest.get(url)
                .header("Authorization", Apikey).asJson();

        JSONObject result = (JSONObject) response.getBody().getObject().get("result"); //json 객체만 뽑아냄
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