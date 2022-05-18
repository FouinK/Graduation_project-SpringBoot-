package com.example.VivaLaTrip.Service;


import com.example.VivaLaTrip.Form.PlaceComputeDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Transactional
@Slf4j
@Service
public class PlanDetailService {


    public List<PlaceComputeDTO> routeCompute(List<PlaceComputeDTO> places, int total_day){

        double sumOfX = 0;
        double sumOfY = 0;
        double sumOfStay = 0;
        int total_count = places.size();

        for (PlaceComputeDTO place : places){
            sumOfX += place.getX();
            sumOfY += place.getY();
            sumOfStay += place.getStay();
        }

        double avgStayOfDays = sumOfStay / total_day;

        if (avgStayOfDays > 8) {
            System.out.println("일평균 시간 : " + avgStayOfDays);
            System.out.println("8시간 초과로 구할 수 없음");
            throw new IllegalStateException("너무 장소 많음");
        } else if (avgStayOfDays < 3) {
            System.out.println("일평균 시간 : " + avgStayOfDays);
            System.out.println("1시간 이하로 구할 수 없음");
            throw new IllegalStateException("너무 장소 적음");
        }

        double avgX = sumOfX / total_count;         //중심 x좌표 (전체)
        double avgY = sumOfY / total_count;         //중심 y좌표 (전체)

        List<PlaceComputeDTO> quadrantOne = new ArrayList<>();
        List<PlaceComputeDTO> quadrantTwo = new ArrayList<>();
        List<PlaceComputeDTO> quadrantThree = new ArrayList<>();
        List<PlaceComputeDTO> quadrantFour = new ArrayList<>();

        for (PlaceComputeDTO place : places) {
            if (place.getX() >= avgX && place.getY() >= avgY) {//1사분면
                quadrantOne.add(place);
            } else if (place.getX() < avgX && place.getY() >= avgY) {//2사분면
                quadrantTwo.add(place);
            } else if (place.getX() <= avgX && place.getY() < avgY) {//3사분면
                quadrantThree.add(place);
            } else if (place.getX() > avgX && place.getY() < avgY) {//4사분면
                quadrantFour.add(place);
            }
        }

        quadrantOne = nearPlaceFirst(sortPlaces(quadrantOne), avgX, avgY);
        quadrantTwo = nearPlaceFirst(sortPlaces(quadrantTwo), avgX, avgY);
        quadrantThree = nearPlaceFirst(sortPlaces(quadrantThree), avgX, avgY);
        quadrantFour = nearPlaceFirst(sortPlaces(quadrantFour), avgX, avgY);

        List<PlaceComputeDTO> sortedPlaces = new ArrayList<>();

        sortedPlaces.addAll(quadrantOne);
        sortedPlaces.addAll(quadrantFour);
        sortedPlaces.addAll(quadrantThree);
        sortedPlaces.addAll(quadrantTwo);

        places = divideDays(sortedPlaces, avgStayOfDays);

        int maxid = 0;
        while (places.get(total_count - 1).getDays() != total_day) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 다를 때 실행 됏음");
            int[] sumStayDays = new int[places.get(total_count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (PlaceComputeDTO place : places) {
                    if (i + 1 == place.getDays()) {
                        sumStayDays[i] += place.getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
                    }
                }
                System.out.println("각 스테이의 합 : " + sumStayDays[i]);
            }
            for (int i = 0; i < sumStayDays.length; i++) {
                if (maxStay < sumStayDays[i]) {
                    maxStay = sumStayDays[i];                                                 //데이즈에서 가장 큰 스테이 합
                    maxid = i + 1;                                                              //최대 스테이합의 데이즈 날짜
                    System.out.println("데이즈가 가장 큰 스테이의 합 :" + maxStay + ", 데이즈에서 가장 큰 스테이의 합의 인덱스(day) : " + maxid);
                }
            }

            if (maxid == places.get(total_count - 1).getDays()) {
                places.get(total_count - 1).setDays(places.get(total_count - 2).getDays() + 1);    //마지막 데이의 스테이 합이 최대일 때 다음데이 넣고              //모든 데이가 일평균을 넘지 못할 수도 있음
                continue;                                                                               //반복문의 맨 처음으로
            }
            if (total_day == maxid) {                                                                        //마지막 days가 들어갔을 때
                break;                                                                                  //반복문 빠져나가기
            }

            int startid = 0;
            for (int i = 0; i < places.size(); i++) {
                if (places.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < places.size(); i++) {
                if (i == total_count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    places.get(i).setDays(places.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }

        System.out.println("마지막 데이가 같을 때까지 넣은 후 : ");
        for (PlaceComputeDTO place : places) {
            System.out.println("[" + place.toString() + "]");
        }

        while (places.get(total_count - 1).getDays() == total_day) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 같을 때 실행 됏음");
            int sumStayDays[] = new int[places.get(total_count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (PlaceComputeDTO place : places) {
                    if (i + 1 == place.getDays()) {
                        sumStayDays[i] += place.getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
                    }
                }
                System.out.println("각 스테이의 합 : " + sumStayDays[i]);
            }
            for (int i = 0; i < sumStayDays.length; i++) {
                if (maxStay < sumStayDays[i]) {
                    maxStay = sumStayDays[i];                                                 //데이즈에서 가장 큰 스테이 합
                    maxid = i + 1;                                                              //최대 스테이합의 데이즈 날짜
                    System.out.println("데이즈가 가장 큰 스테이의 합 :" + maxStay + ", 데이즈에서 가장 큰 스테이의 합의 인덱스(day) : " + maxid);
                }
            }

            if (sumStayDays[total_day-1] >= avgStayOfDays/2) {                                                 //마지막 데이의 스테이 합이 평균 보다 1/2클 때
                break;                                                                                 //반복문 종료
            }


            int startid = 0;
            for (int i = 0; i < places.size(); i++) {
                if (places.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < places.size(); i++) {
                if (i == total_count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    places.get(i).setDays(places.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }

        return places;
    }

    public List<PlaceComputeDTO> setSlope(double avgX, double avgY, List<PlaceComputeDTO> places){
        for (PlaceComputeDTO place : places){
            if (place.getX() > avgX){  //
                place.setSlope((place.getY() - avgY) / (place.getX() - avgX));
                place.setWhere("right");
            }else if(place.getX() < avgX){
                place.setSlope((place.getX() - avgX) / (place.getY() - avgY));
                place.setWhere("left");
            }else if(place.getX() == avgX){
                place.setSlope(999999);
                if (place.getY() >= avgY){
                    place.setWhere("right");
                }else {
                    place.setWhere("left");
                }
            }
        }
        return places;
    }

    public List<PlaceComputeDTO> sortPlaces(List<PlaceComputeDTO> places){
        double avgX=0;
        double avgY=0;

        for (PlaceComputeDTO place : places){
            avgX=+place.getX();
            avgY=+place.getY();
        }
        avgX = avgX / places.size();
        avgY = avgY / places.size();

        places = setSlope(avgX,avgY,places);

        List<PlaceComputeDTO> rightList = new ArrayList<>();
        List<PlaceComputeDTO> leftList = new ArrayList<>();

        for (PlaceComputeDTO place : places){
            if (place.getWhere().equals("right")){
                rightList.add(place);
            }else {
                leftList.add(place);
            }
        }
        List<PlaceComputeDTO> result = new ArrayList<>();

        Collections.sort(rightList);
        Collections.sort(leftList);

        result.addAll(rightList);
        result.addAll(leftList);

        return result;
    }

    public List<PlaceComputeDTO> nearPlaceFirst(List<PlaceComputeDTO> places, double avgX, double avgY){

        double minDistance = 999999999;
        int index = 0;
        int minDistanceIndex=0;

        for (PlaceComputeDTO place : places){

            double distance = (avgX-place.getX()) * (avgX-place.getX())
                    + (avgY-place.getY()) * (avgY-place.getY());   //x^2+y^2=d^2
            if (minDistance>distance){
                minDistance = distance;
                minDistanceIndex = index;
            }
            index++;
        }

        List<PlaceComputeDTO> resultList = new ArrayList<>();

        resultList.addAll(places.subList(minDistanceIndex,places.size()));
        resultList.addAll(places.subList(0,minDistanceIndex));

        return resultList;
    }

    public List<PlaceComputeDTO> divideDays(List<PlaceComputeDTO> places, double avgStayOfDays) {        //데이 생성해서 담기

        int stayOfDay=0;
        int day = 1;

        for (PlaceComputeDTO place : places) {
            if (stayOfDay < avgStayOfDays) {
                place.setDays(day);
                stayOfDay += place.getStay();
            } else {
                stayOfDay =0;
                day++;
                place.setDays(day);
                stayOfDay += place.getStay();
            }
        }
        return places;
    }
}
