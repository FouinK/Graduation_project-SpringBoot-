package com.example.VivaLaTrip.Service;

import com.example.VivaLaTrip.Entity.Documents;
import com.example.VivaLaTrip.Entity.KakaoGeoRes;
import com.example.VivaLaTrip.Entity.Places;
import com.example.VivaLaTrip.Form.PlanDetailDTO;
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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ResponseBody;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

//Places요청에 의한 실질적인 내부 동작 로직을 처리하는 MapService클래스
@Slf4j
public class MapService {

    private final MapRepository mapRepository;

    public MapService(MapRepository mapRepository) {
        this.mapRepository = mapRepository;
    }

    //자체 데이터 베이스 저장 전 Places 값들을 호출하기 위해 사용 됐던 KAKAO MAP의 명소 호출 함수(45개)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ResponseBody
    public List<Documents> MapParsing(String word) throws UnsupportedEncodingException, UnirestException, JsonProcessingException {

        String APIKey = "KakaoAK ";                         //kakao에서 제공하는 API KEY를 담는 변수

        /*주요코드*/
        //카카오에서 제공하는 API호출을 위한 Url로 KAKAO가 가지고 있는 명소 값을을 뽑아옴(키워드를 입력 받아야 가능 ..ex)서울 명소
        //한 번 요청에 최대 15개가 최대이므로 총 3번 요청함
        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        String apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=1&query="
                + URLEncoder.encode(word, StandardCharsets.UTF_8);

        HttpResponse<JsonNode>[] response= new HttpResponse[3];

        response[0] = Unirest.get(apiURL)                   //Page1에 담긴 15개의 장소를 담음
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=2&query="
                + URLEncoder.encode(word, "UTF-8");

        response[1] = Unirest.get(apiURL)                   //Page2에 담긴 15개의 장소를 담음
                .header("Authorization", APIKey)
                .asJson();

        apiURL ="https://dapi.kakao.com/v2/local/search/keyword.json?page=3&query="
                + URLEncoder.encode(word, "UTF-8");

        response[2] = Unirest.get(apiURL)                   //Page3에 담긴 15개의 장소를 담음
                .header("Authorization", APIKey)
                .asJson();

        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        KakaoGeoRes bodyJson[] = new KakaoGeoRes[3];        //원하는 key값만 뽑아내기 위해 자체적으로 만든 객체 호출

        //Places 15개씩 객체배열에 담음
        bodyJson[0] = objectMapper.readValue(response[0].getBody().toString(), KakaoGeoRes.class);
        bodyJson[1] = objectMapper.readValue(response[1].getBody().toString(), KakaoGeoRes.class);
        bodyJson[2] = objectMapper.readValue(response[2].getBody().toString(), KakaoGeoRes.class);

        List<Documents> result = new ArrayList<>();         //Places에 담긴 x,y값 및 주소, 장소 이름 등이 실제적으로 담기는 객체 호출(KAKAO에서 제공하는 key값들에 맞추어 자체 제작)

        //45개 Places에 대한 모든 정보를 ArrayList에 합침.
        result.addAll(bodyJson[0].getDocuments());
        result.addAll(bodyJson[1].getDocuments());
        result.addAll(bodyJson[2].getDocuments());

        return result;  //45개에 대한 장소정보 리턴
    }

    //14만개의 places데이터를 담고있는 데이터베이스를 참조하여 원하는 Places값을 뽑아오는 함수
    public List<Places> MapParsingDB(String word) {

        //사용자가 원하는 키워드가 포함된 모든 Places의 데이터를 뽑아옴(인기순으로 정렬)
        List<Places> bodyJson = mapRepository.findByAddressNameContains(word, Sort.by(Sort.Order.desc("popularity")));

        //Places의 개수를 100개 이하로 잘라서 리턴.
        if (bodyJson.size() > 100) {
            return bodyJson.subList(0, 100);
        }
        return bodyJson;
    }

    //경로 계산 알고리즘시 동작 되는 함수(중심 좌표 2키로 반경 숙소 값 파싱)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ResponseBody
    public PlanDetailDTO getHotel(double x, double y) throws UnirestException, JsonProcessingException {

        /*주요코드*/
        //카카오에서 제공하는 API호출을 위한 Url로 KAKAO가 가지고 있는 숙소 값을을 뽑아옴 중심 x,y좌표 기준 2키로 반경
        String APIKey = "KakaoAK ";
        String apiURL ="https://dapi.kakao.com/v2/local/search/category.json?" +
                "page=1&category_group_code=AD5&x="+x+"&y="+y+"&radius=20000&sort=distance";

        HttpResponse<JsonNode> response = Unirest.get(apiURL)
                .header("Authorization", APIKey).asJson();

        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        //숙소들에 담긴 x,y값 및 주소, 장소 이름 등이 실제적으로 담기는 객체 호출(KAKAO에서 제공하는 key값들에 맞추어 자체 제작)
        KakaoGeoRes bodyJson = objectMapper.readValue(response.getBody().toString(), KakaoGeoRes.class);
        List<Documents> result = new ArrayList<>(bodyJson.getDocuments());

        PlanDetailDTO hotel = PlanDetailDTO.builder()
                .id(result.get(0).getId())
                .place_name(result.get(0).getPlace_name())
                .x(Double.parseDouble(result.get(0).getX()))
                .y(Double.parseDouble(result.get(0).getY()))
                .build();

        return hotel;   //15개의 숙소 값 리턴
    }

    //사용자가 Plan을 만들 때 장소 개수에 비해 일정이 지나치게 짧아 의도한 대로 알고리즘이 생성되지 않을 때 자체적으로 장소를 추천하여 일정에 장소들을 추가해주는 함수
    public List<Places> placeAdd(List<Places> places, double minPlace) {

        int avgPopularity = 0;
        double x_max = places.get(0).getX();
        double x_min = places.get(0).getX();
        double y_max = places.get(0).getY();
        double y_min = places.get(0).getY();

        for (Places p : places){
            if (x_max < p.getX()){
                x_max = p.getX();
            }if (x_min > p.getX()){
                x_min = p.getX();
            }if (y_max < p.getY()){
                y_max = p.getY();
            }if (y_min > p.getY()){
                y_min = p.getY();
            }
            avgPopularity += p.getPopularity();
        }

        avgPopularity = avgPopularity / places.size();
        double distance = 0.005;  //500m
        int index = 0;  //반복 횟수 - 범위를 점차 늘리기 위해

        //사용자가 입력한 장소들의 500미터 범위에 있는 모든 Places를 뽑아옴 그래도 개수가 부족하다면 반경을 점점 500미터 씩 늘려서 장소를 뽑아옴
        while (minPlace > places.size()){
            List<Places> extraPlaces = mapRepository.findByXBetweenAndYBetween(
                    (x_min - distance * index),
                    (x_max + distance * index),
                    (y_min - distance * index),
                    (y_max + distance * index),
                    Sort.by(Sort.Order.desc("popularity")));
            //사용자로부터 입력받은 모든 Places들의 인기도 평균을 계산하여 인기도가 그 이상인 Places들만 솎아냄
            for (Places extraPlace : extraPlaces){
                if (!places.contains(extraPlace) && extraPlace.getPopularity() > avgPopularity){
                    places.add(extraPlace);
                }
            }
            index++;
        }

        return places;      //추천된 장소들을 리턴
    }

/*    public void Save_Places(KakaoGeoRes bodyJson[]) {
//        log.info("세이브 메소드에서 받은 bodyJson id값 : " + bodyJson[0].getDocuments().get(0).getId());

        Places[] places = new Places[15];
        for (int i = 0; i < 15; i++) {
            places[i] = new Places();
        }
//        places[0].setPlace_id(bodyJson[0].getDocuments().get(0).getId());
//        log.info("Places배열 정상 작동"+places[0].getPlace_id());

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 15; j++) {
                String category_depth = splitCategory(bodyJson[i].getDocuments().get(j).getCategory_name());
                places[j].setPlace_id(bodyJson[i].getDocuments().get(j).getId());
                places[j].setPlace_name(bodyJson[i].getDocuments().get(j).getPlace_name());
                places[j].setAddress_name(bodyJson[i].getDocuments().get(j).getAddress_name());
                places[j].setPhone(bodyJson[i].getDocuments().get(j).getPhone());
                places[j].setUrl(bodyJson[i].getDocuments().get(j).getPlace_url());
                places[j].setX(bodyJson[i].getDocuments().get(j).getX());
                places[j].setY(bodyJson[i].getDocuments().get(j).getY());
                places[j].setCategory_name(category_depth);
                places[j].setIndoor("0");
                mapRepository.save(places[j]);
            }
        }
//        log.info("for문 적상 작동");
//        log.info("맵 리포지토리 정상 작동");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @ResponseBody
    public void MapParsing2() throws UnirestException, JsonProcessingException {
        *//*대한민국
        최북단 : 강원도 고성군 현내면 대강리 *38.61503025970676 128.3580969588429
        최남단 : 제주특별자치도 서귀포시 대정읍 마라리 *33.11260291103729 126.26834665821991
        최동단 : 경상북도 울릉군 울릉읍 독도리 동도 37.23981774230282 *131.8727302252229945
        최서단 : 인천광역시 옹진군 백령면 연화리. 37.96878377086497 *124.60976081654518
        육지 최동단 : 포항 어딘가 위도 36.01670936347706, 경도 129.5845724459377

        최븍서(좌상단 꼭지점) : 38.61503025970676 124.60976081654518
        최남동(우하단 꼭지점) : 33.11260291103729 131.8727302252229945
        육지 최남동(우하단 꼭지점) : 33.11260291103729 129.5845724459377

        테스트용 경복궁 좌표 : 37.57740 126.97679
        *//*
        String APIKey = "KakaoAK";
//        ArrayList<String> result = new ArrayList<>();

        double distance = 0.01000; //1000m
        double farEast = 131.8727302252229945;
        double farSouth = 35.61503025970676;
        double farWest = 124.60976081654518;
        double farNorth = 36.70503025970684;    //바로 시작하면 됨        //37.11503025970676 부터 함        //38.61503025970676 ~ 38.415030259706796 함
        double y = farNorth;

        while (y > farSouth) {

            double x = farWest;

            while (x < farEast) {
                for(int i=1; i<4; i++){
                    String apiURL = "https://dapi.kakao.com/v2/local/search/category.json?" +
                            "category_group_code=AT4&" +
                            "rect=" + x + "," + y + "," + (x + distance) + "," + (y - distance) + "&" +
                            "page="+i;

                    HttpResponse<JsonNode> response = Unirest
                            .get(apiURL)
                            .header("Authorization", APIKey).asJson();

                    ObjectMapper objectMapper =new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                    JSONObject jsonObject = response.getBody().getObject();
                    JSONObject meta = (JSONObject) jsonObject.get("meta");


//                    System.out.println("리스펀스 값 : "+response.getBody().toString());



                    if (meta.get("total_count") == "0"){      //검색결과 없으면 탈출
                        break;
                    } else {                                    //아니면 저장
                        KakaoGeoRes bodyJson = objectMapper.readValue(response.getBody().toString(), KakaoGeoRes.class);
                        Save_Places2(bodyJson);
                    }

                    if ((boolean) meta.get("is_end")) {               //마지막페이지면 탈출
                        break;
                    }
                }
                x += distance;          //x값 플러스
            }

            System.out.println("중간 Y값 : "+y);

            y -= distance;              //y값 마이너스
        }
    }

    public String splitCategory(String category){
        if(category.length()!=10){  //3번째 이상 카테고리 있을 경우
            return category.substring(13);
        }
        else{  //2번째 카테고리까지 있는 경우
            return "";
        }
    }
    public void Save_Places2(KakaoGeoRes bodyJson) {
//        log.info("세이브 메소드에서 받은 bodyJson id값 : " + bodyJson[0].getDocuments().get(0).getId());
        int size = bodyJson.getDocuments().size();  //반복문 속도 개선 - 변수 지정하기
        Places[] places = new Places[size];
        for (int i = 0; i < size; i++) {
            places[i] = new Places();
        }
//        places[0].setPlace_id(bodyJson[0].getDocuments().get(0).getId());
//        log.info("Places배열 정상 작동"+places[0].getPlace_id());


        for (int j = 0; j < size; j++) {
//            log.info("세이브플레이스2 바디제이슨 카테고리 값  : " +bodyJson.getDocuments().get(j).getCategory_name());
            String category_depth = splitCategory(bodyJson.getDocuments().get(j).getCategory_name());
            places[j].setPlace_name(bodyJson.getDocuments().get(j).getPlace_name());
            places[j].setAddress_name(bodyJson.getDocuments().get(j).getAddress_name());
            places[j].setUrl(bodyJson.getDocuments().get(j).getPlace_url());
            places[j].setX(bodyJson.getDocuments().get(j).getX());
            places[j].setY(bodyJson.getDocuments().get(j).getY());
            places[j].setPhone(bodyJson.getDocuments().get(j).getPhone());
            places[j].setPlace_id(bodyJson.getDocuments().get(j).getId());
            places[j].setCategory_name(category_depth);
            places[j].setIndoor("0");
            mapRepository.save(places[j]);
        }
//        log.ifo("for문 적상 작동");
//        log.info("맵 리포지토리 정상 작동");
    }*/
}