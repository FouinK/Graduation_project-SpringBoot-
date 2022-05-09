package com.example.VivaLaTrip;

import com.example.VivaLaTrip.Entity.Places;
//import com.example.VivaLaTrip.OpenWeatherDto.OpenWeather;

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

import java.util.*;

@SpringBootTest
@Transactional
public class VivaLaTripApplicationTests {

    @Test
    String test() {
        return "test complete";
    }

    @Test
    public void RouteComputeTest5route() {                       //시계방향 5번
        Random ran = new Random();
//        int count = ran.nextInt(50) + 10;             //10개~50개 장소 랜덤 값
        int count = 27;             //10개~50개 장소 랜덤 값

//        int days = ran.nextInt(10) + 2;               //2~10days 랜덤
        int days = 10;               //2~10days 랜덤

        System.out.println("장소 개수 :" + count);
        System.out.println("데이수 :" + days);

        PlacesTest placesTest[] = new PlacesTest[count];        //장소 개수에 맞게 클래스 배여 선언
        for (int i = 0; i < placesTest.length; i++) {           //초기화
            placesTest[i] = new PlacesTest();
        }

        double sumX = 0;
        double sumY = 0;
        float sumStay = 0;

        System.out.println("처음 담을 떄 값 :");
        for (int i = 0; i < placesTest.length; i++) {
            placesTest[i].setX((ran.nextInt(130) + 30) + ran.nextDouble());     //x값 임의로 값 생성해서 넣기
            placesTest[i].setY(ran.nextInt(30) + ran.nextDouble());           //y값 임의로 값 생성해서 넣기
            placesTest[i].setStay(ran.nextInt(3) + 1);                        //머물시간 임의로 값 생성해서 넣기
//            placesTest[i].setStay(2);                        //머물시간 임의로 값 생성해서 넣기

            sumX += placesTest[i].getX();                                           //x값의 합 구하기
            sumY += placesTest[i].getY();                                           //y값의 합 구하기
            sumStay += placesTest[i].getStay();                                     //stay값의 합 구하기
            System.out.println("[" + placesTest[i].toString() + "]");
        }

//        placesTest = Avg("전체",placesTest,count);

        System.out.println("전체 장소 머물 시간 :" + sumStay);
        float dayStayAvg = sumStay / days;

        //일 평균 시간을 구해서 구할 수 있는지 없는지 판별

        if (dayStayAvg > 8) {
            System.out.println("일평균 시간 : " + dayStayAvg);
            System.out.println("8시간 초과로 구할 수 없음");
            return;
        } else if (dayStayAvg < 3) {
            System.out.println("일평균 시간 : " + dayStayAvg);
            System.out.println("1시간 이하로 구할 수 없음");
            return;
        }

        System.out.println("일평균 시간 : " + dayStayAvg);
        System.out.println("최적의 시간으로 구할 수 있음");

        //x,y,stay 값 출력 테스트
        System.out.print("x값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i + "번째 : [" + placesTest[i].getX() + "], ");
        }

        System.out.println();
        System.out.print("y값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i + "번째 : [" + placesTest[i].getY() + "], ");
        }
        System.out.println();
        System.out.print("stay값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i + "번째 : [" + placesTest[i].getStay() + "시간], ");
        }
        System.out.println();

        double avgX = sumX / count;         //x값 평균 구하기 -> 중심 x좌표 (전체)
        System.out.println("전체의 x값 평균 : " + avgX);
        double avgY = sumY / count;         //y값 평균 구하기 -> 중심 y좌표 (전체)
        System.out.println("전체의 y값 평균 : " + avgY);

        int page1 = 0;
        int page2 = 0;
        int page3 = 0;
        int page4 = 0;
        //클래스 배열에 넣기
        for (int i = 0; i < placesTest.length; i++) {
            if (placesTest[i].getX() >= avgX && placesTest[i].getY() > avgY) {      //1사분면에 위치하거나 기울기가 0일 때(x값이 중심x 값보다 큼)
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("page1");
                page1++;
            } else if (placesTest[i].getX() < avgX && placesTest[i].getY() > avgY) {       //2사분면에 위치할 때
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("page2");
                page2++;
            } else if (placesTest[i].getX() <= avgX && placesTest[i].getY() < avgY) {         //3사분면에 위치하거나 기울기가 0일 때(x값이 중심x 값보다 작음)
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("page3");
                page3++;
            } else if (placesTest[i].getX() > avgX && placesTest[i].getY() < avgY) {        //4사분면에 위치할 때
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("page4");
                page4++;
            } else if (placesTest[i].getX() == avgX && placesTest[i].getY() < avgY) {       //기울기는 없고 y절편이 -일 때
                placesTest[i].setSlope(10000000);        //3사분면에 위치하는 배열에 넣기 위해
                placesTest[i].setWhere("page3");
                page3++;
            } else if (placesTest[i].getX() == avgX && placesTest[i].getY() > avgY) {       //기울기는 없고 y절편이 +일 때
                placesTest[i].setSlope(10000000);        //1사분면에 위치하는 배열에 넣기 위해
                placesTest[i].setWhere("page1");
                page1++;
            }
        }

        PlacesTest one[] = new PlacesTest[page1];        //1사분면 개수만큼 배열 만들기
        PlacesTest two[] = new PlacesTest[page2];        //2사분면 개수만큼 배열 만들기
        PlacesTest three[] = new PlacesTest[page3];        //3사분면 개수만큼 배열 만들기
        PlacesTest four[] = new PlacesTest[page4];        //4사분면 개수만큼 배열 만들기

        for (int i = 0; i < one.length; i++) {           //1초기화
            one[i] = new PlacesTest();
        }

        for (int i = 0; i < two.length; i++) {           //2초기화
            two[i] = new PlacesTest();
        }

        for (int i = 0; i < three.length; i++) {           //3초기화
            three[i] = new PlacesTest();
        }

        for (int i = 0; i < four.length; i++) {           //4초기화
            four[i] = new PlacesTest();
        }

        int ichi = 0;
        int nee = 0;
        int san = 0;
        int si = 0;
//        System.out.print("1,4사분면 배열 값 :");
        for (int i = 0; i < placesTest.length; i++) {
            if (placesTest[i].getWhere().equals("page1")) {                 //1사분면 일 때
                one[ichi] = placesTest[i];      //1사분면만 옮겨 담기
                ichi++;
            } else if (placesTest[i].getWhere().equals("page2")) {          //2사분면 일 때
                two[nee] = placesTest[i];     //2사분면만 옮겨 담기
                nee++;
            } else if (placesTest[i].getWhere().equals("page3")) {          //3사분면 일 때
                three[san] = placesTest[i];     //3사분면만 옮겨 담기
                san++;
            } else if (placesTest[i].getWhere().equals("page4")) {          //4사분면 일 때
                four[si] = placesTest[i];     //4사분면만 옮겨 담기
                si++;
            }
        }
        //------------------------------------------------------
        double sumX1 = 0;
        double sumY1 = 0;
        double avgX1 = 0;
        double avgY1 = 0;
        System.out.print("1사분면 : ");
        for (int i = 0; i < one.length; i++) {
            System.out.println(one[i].toString());
            sumX1 += one[i].getX();                                           //x값의 합 구하기
            sumY1 += one[i].getY();                                           //y값의 합 구하기
        }
        avgX1 = sumX1 / one.length;         //x값 평균 구하기 -> 중심 x좌표 (1사분면)
        System.out.println("1사분면 x값 평균 : " + avgX1);
        avgY1 = sumY1 / one.length;         //y값 평균 구하기 -> 중심 y좌표 (1사분면)
        System.out.println("1사분면 y값 평균 : " + avgY1);

        List<PlacesTest> oneList = avgSlope(one, avgX1, avgY1, avgX, avgY, "one", 1);

        //------------------------------------------------------

        double sumX2 = 0;
        double sumY2 = 0;
        double avgX2 = 0;
        double avgY2 = 0;
        System.out.print("2사분면");
        for (int i = 0; i < two.length; i++) {
            System.out.println(two[i].toString());
            sumX2 += two[i].getX();                                           //x값의 합 구하기
            sumY2 += two[i].getY();                                           //y값의 합 구하기
        }
        avgX2 = sumX2 / two.length;         //x값 평균 구하기 -> 중심 x좌표 (2사분면)
        System.out.println("2사분면 x값 평균 : " + avgX2);
        avgY2 = sumY2 / two.length;         //y값 평균 구하기 -> 중심 y좌표 (2사분면)
        System.out.println("2사분면 y값 평균 : " + avgY2);

        List<PlacesTest> twoList = avgSlope(two, avgX2, avgY2, avgX, avgY, "two", 2);

        //------------------------------------------------------

        double sumX3 = 0;
        double sumY3 = 0;
        double avgX3 = 0;
        double avgY3 = 0;
        System.out.print("3사분면");
        for (int i = 0; i < three.length; i++) {
            System.out.println(three[i].toString());
            sumX3 += three[i].getX();                                           //x값의 합 구하기
            sumY3 += three[i].getY();                                           //y값의 합 구하기
        }
        avgX3 = sumX3 / three.length;         //x값 평균 구하기 -> 중심 x좌표 (3사분면)
        System.out.println("3사분면 x값 평균 : " + avgX3);
        avgY3 = sumY3 / three.length;         //y값 평균 구하기 -> 중심 y좌표 (3사분면)
        System.out.println("3사분면 y값 평균 : " + avgY3);

        List<PlacesTest> threeList = avgSlope(three, avgX3, avgY3, avgX, avgY, "three", 3);

        //------------------------------------------------------

        double sumX4 = 0;
        double sumY4 = 0;
        double avgX4 = 0;
        double avgY4 = 0;
        System.out.print("4사분면");
        for (int i = 0; i < four.length; i++) {
            System.out.println(four[i].toString());
            sumX4 += four[i].getX();                                           //x값의 합 구하기
            sumY4 += four[i].getY();                                           //y값의 합 구하기
        }
        avgX4 = sumX4 / four.length;         //x값 평균 구하기 -> 중심 x좌표 (4사분면)
        System.out.println("4사분면 x값 평균 : " + avgX4);
        avgY4 = sumY4 / four.length;         //y값 평균 구하기 -> 중심 y좌표 (4사분면)
        System.out.println("4사분면 y값 평균 : " + avgY4);

        List<PlacesTest> fourList = avgSlope(four, avgX4, avgY4, avgX, avgY, "four", 4);

        List<PlacesTest> placesTests = new ArrayList<>();       //최종적으로 담을 리스트 선언

        placesTests.addAll(oneList);
        placesTests.addAll(fourList);
        placesTests.addAll(threeList);
        placesTests.addAll(twoList);

        System.out.print("합친 배열 리스트 출력 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + placesTests.get(i).toString() + "]");
        }

        List<PlacesTest> finalPlacesTest = divideDays(placesTests, dayStayAvg, days, count);     //날짜 나누어 담기

        int maxid = 0;
        System.out.println("마지막 날 다를 때 한칸 씩 앞으로 땡기기 테스트 전 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }


        while (finalPlacesTest.get(count - 1).getDays() != days) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 다를 때 실행 됏음");
            int sumStayDays[] = new int[finalPlacesTest.get(count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (int j = 0; j < finalPlacesTest.size(); j++) {
                    if (i + 1 == finalPlacesTest.get(j).getDays()) {
                        sumStayDays[i] += finalPlacesTest.get(j).getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
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

            if (maxid == finalPlacesTest.get(count - 1).getDays()) {
                finalPlacesTest.get(count - 1).setDays(finalPlacesTest.get(count - 2).getDays() + 1);    //마지막 데이의 스테이 합이 최대일 때 다음데이 넣고              //모든 데이가 일평균을 넘지 못할 수도 있음
                continue;                                                                               //반복문의 맨 처음으로
            }
            if (days == maxid) {                                                                        //마지막 days가 들어갔을 때
                break;                                                                                  //반복문 빠져나가기
            }

            int startid = 0;
            for (int i = 0; i < finalPlacesTest.size(); i++) {
                if (finalPlacesTest.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < finalPlacesTest.size(); i++) {
                if (i == count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    finalPlacesTest.get(i).setDays(finalPlacesTest.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }

        System.out.println("마지막 데이가 같을 때까지 넣은 후 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }

        while (finalPlacesTest.get(count - 1).getDays() == days) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 같을 때 실행 됏음");
            int sumStayDays[] = new int[finalPlacesTest.get(count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (int j = 0; j < finalPlacesTest.size(); j++) {
                    if (i + 1 == finalPlacesTest.get(j).getDays()) {
                        sumStayDays[i] += finalPlacesTest.get(j).getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
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

            if (sumStayDays[days-1] >= dayStayAvg/2) {                                                 //마지막 데이의 스테이 합이 평균 보다 1/2클 때
                break;                                                                                 //반복문 종료
            }


            int startid = 0;
            for (int i = 0; i < finalPlacesTest.size(); i++) {
                if (finalPlacesTest.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < finalPlacesTest.size(); i++) {
                if (i == count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    finalPlacesTest.get(i).setDays(finalPlacesTest.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }


        System.out.println("데이즈 값 :" + days);
        System.out.println("마지막 테스트 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }

        System.out.println("test good!!");

    }

    @Test
    public List<PlacesTest> avgSlope(PlacesTest[] arr, double avgX1, double avgY1, double allavgX, double allavgY, String word, int whrere) {
        int onepage1 = 0;
        int onepage2 = 0;
        int onepage3 = 0;
        int onepage4 = 0;
        //클래스 배열에 넣기
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getX() >= avgX1 && arr[i].getY() > avgY1) {      //1사분면에 위치하거나 기울기가 0일 때(x값이 중심x 값보다 큼)
                arr[i].setSlope((arr[i].getY() - avgY1) / (arr[i].getX() - avgX1));
                arr[i].setWhere("page" + whrere + "one");
                onepage1++;
            } else if (arr[i].getX() < avgX1 && arr[i].getY() > avgY1) {       //2사분면에 위치할 때
                arr[i].setSlope((arr[i].getY() - avgY1) / (arr[i].getX() - avgX1));
                arr[i].setWhere("page" + whrere + "two");
                onepage2++;
            } else if (arr[i].getX() <= avgX1 && arr[i].getY() < avgY1) {         //3사분면에 위치할 거나 기울기가 0일 때(x값이 중심x 값보다 작음)
                arr[i].setSlope((arr[i].getY() - avgY1) / (arr[i].getX() - avgX1));
                arr[i].setWhere("page" + whrere + "three");
                onepage3++;
            } else if (arr[i].getX() > avgX1 && arr[i].getY() < avgY1) {        //4사분면에 위치할 때
                arr[i].setSlope((arr[i].getY() - avgY1) / (arr[i].getX() - avgX1));
                arr[i].setWhere("page" + whrere + "four");
                onepage4++;
            } else if (arr[i].getX() == avgX1 && arr[i].getY() < avgY1) {       //기울기는 없고 y절편이 -일 때
                arr[i].setSlope(10000000);        //3사분면에 위치하는 배열에 넣기 위해
                arr[i].setWhere("page" + whrere + "three");
                onepage3++;
            } else if (arr[i].getX() == avgX1 && arr[i].getY() > avgY1) {       //기울기는 없고 y절편이 +일 때
                arr[i].setSlope(10000000);        //1사분면에 위치하는 배열에 넣기 위해
                arr[i].setWhere("page" + whrere + "one");
                onepage1++;
            }
        }

        PlacesTest one1[] = new PlacesTest[onepage1];        //1사분면 개수만큼 배열 만들기
        PlacesTest two1[] = new PlacesTest[onepage2];        //2사분면 개수만큼 배열 만들기
        PlacesTest three1[] = new PlacesTest[onepage3];        //3사분면 개수만큼 배열 만들기
        PlacesTest four1[] = new PlacesTest[onepage4];        //4사분면 개수만큼 배열 만들기

        for (int i = 0; i < one1.length; i++) {           //1초기화
            one1[i] = new PlacesTest();
        }

        for (int i = 0; i < two1.length; i++) {           //2초기화
            two1[i] = new PlacesTest();
        }

        for (int i = 0; i < three1.length; i++) {           //3초기화
            three1[i] = new PlacesTest();
        }

        for (int i = 0; i < four1.length; i++) {           //4초기화
            four1[i] = new PlacesTest();
        }

        int ichi = 0;
        int nee = 0;
        int san = 0;
        int si = 0;
//        System.out.print("1,4사분면 배열 값 :");
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].getWhere().equals("page" + whrere + "one")) {                 //1사분면 일 때
                one1[ichi] = arr[i];      //1사분면만 옮겨 담기
                ichi++;
            } else if (arr[i].getWhere().equals("page" + whrere + "two")) {          //2사분면 일 때
                two1[nee] = arr[i];     //2사분면만 옮겨 담기
                nee++;
            } else if (arr[i].getWhere().equals("page" + whrere + "three")) {          //3사분면 일 때
                three1[san] = arr[i];     //3사분면만 옮겨 담기
                san++;
            } else if (arr[i].getWhere().equals("page" + whrere + "four")) {          //4사분면 일 때
                four1[si] = arr[i];     //4사분면만 옮겨 담기
                si++;
            }
        }

        //내부 1~4사분면도 최대~최소 기울기 구해서 담음
        one1 = onethreeRoute(one1);
        two1 = twofourRoute(two1);
        three1 = onethreeRoute(three1);
        four1 = twofourRoute(four1);

        //1,4,3,2순서대로 합치기 위해
        List<PlacesTest> List1 = Arrays.asList(one1);
        List<PlacesTest> List2 = Arrays.asList(two1);
        List<PlacesTest> List3 = Arrays.asList(three1);
        List<PlacesTest> List4 = Arrays.asList(four1);

        List<PlacesTest> placesTests = new ArrayList<>();       //최종적으로 담을 리스트 선언

        //1,4,3,2 순서대로 합침
        placesTests.addAll(List1);
        placesTests.addAll(List4);
        placesTests.addAll(List3);
        placesTests.addAll(List2);

        return placesTests;
    }

    @Test
    public PlacesTest[] onethreeRoute(PlacesTest[] arr13) {      //1,3사분면 나누어 담기 > 기울기 때문에
        //기울기 양수거나 0일 때
        int maxid = 0;
        for (int i = 0; i < arr13.length; i++) {

            double max = arr13[0].getSlope();
            for (int n = i; n < arr13.length; n++) {
                if (max < arr13[n].getSlope()) {
                    max = arr13[n].getSlope();
                    maxid = n;
                }
            }
            PlacesTest temp = arr13[i];
            arr13[i] = arr13[maxid];
            arr13[maxid] = temp;
        }
        return arr13;
    }

    @Test
    public PlacesTest[] twofourRoute(PlacesTest[] arr24) {      //2,4사분면 나누어 담기 > 기울기 때문에
        //기울기 음수 일 때
        int minid = 0;
        for (int i = 0; i < arr24.length; i++) {
            double min = arr24[0].getSlope();
            for (int n = i; n < arr24.length; n++) {
                if (min > arr24[n].getSlope()) {
                    min = arr24[n].getSlope();
                    minid = n;
                }
            }
            PlacesTest temp = arr24[i];
            arr24[i] = arr24[minid];
            arr24[minid] = temp;
        }
        return arr24;
    }

    @Test
    public List<PlacesTest> divideDays(List<PlacesTest> arr, float dayStayAvg, int days, int count) {        //데이 생성해서 담기
        int daysIn = 1;
        double stayAvg = 0;
        for (int i = 0; i < arr.size(); i++) {
            stayAvg += arr.get(i).getStay();
            if (daysIn == days) {
                arr.get(i).setDays(days);
            } else if (dayStayAvg > stayAvg) {
                arr.get(i).setDays(daysIn);
            } else if (dayStayAvg <= stayAvg) {
                arr.get(i).setDays(daysIn);
                daysIn++;
                stayAvg = 0;
            }
        }
        return arr;
    }

    @Test
    public void RouteComputeTest_clock1route()  {                               //시계방향 1번
        Random ran = new Random();
        int count = ran.nextInt(50)+10;             //10개의 장소 값
        int days = ran.nextInt(20)+2;

        System.out.println("장소 개수 :" + count);
        System.out.println("데이수 :" + days);

        PlacesTest placesTest[] = new PlacesTest[count];        //장소 개수에 맞게 클래스 배여 선언
        for (int i = 0; i < placesTest.length; i++) {           //초기화
            placesTest[i] = new PlacesTest();
        }

        double sumX = 0;
        double sumY = 0;
        float sumStay = 0;

        System.out.println("처음 담을 떄 값 :");
        for (int i = 0; i < placesTest.length; i++) {
            placesTest[i].setX((ran.nextInt(130)+30)+ran.nextDouble());     //x값 임의로 값 생성해서 넣기
            placesTest[i].setY(ran.nextInt(30)+ran.nextDouble());           //y값 임의로 값 생성해서 넣기
            placesTest[i].setStay(ran.nextInt(3) + 1);                        //머물시간 임의로 값 생성해서 넣기

            sumX += placesTest[i].getX();                                           //x값의 합 구하기
            sumY += placesTest[i].getY();                                           //y값의 합 구하기
            sumStay += placesTest[i].getStay();                                     //stay값의 합 구하기
            System.out.println("["+placesTest[i].toString()+"]");

        }

        System.out.println("전체 장소 머물 시간 :"+sumStay);
        float dayStayAvg = sumStay / days;

        //일 평균 시간을 구해서 구할 수 있는지 없는지 판별
        if (dayStayAvg > 8) {
            System.out.println("일평균 시간 : "+dayStayAvg);
            System.out.println("8시간 초과로 구할 수 없음");
        } else if (dayStayAvg < 4) {
            System.out.println("일평균 시간 : "+dayStayAvg);
            System.out.println("1시간 이하로 구할 수 없음");
        }

        System.out.println("일평균 시간 : "+dayStayAvg);
        System.out.println("최적의 시간으로 구할 수 있음");

        //x,y,stay 값 출력 테스트
        System.out.print("x값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i+"번째 : ["+placesTest[i].getX()+"], ");
        }

        System.out.println();
        System.out.print("y값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i+"번째 : ["+placesTest[i].getY()+"], ");
        }
        System.out.println();
        System.out.print("stay값 : ");
        for (int i = 0; i < placesTest.length; i++) {
            System.out.print(i+"번째 : ["+placesTest[i].getStay()+"시간], ");
        }
        System.out.println();

        double avgX = sumX / count;         //x값 평균 구하기
        System.out.println("x값 평균 : "+avgX);
        double avgY = sumY / count;         //y값 평균 구하기
        System.out.println("y값 평균 : "+avgY);

        int rightCnt = 0;
        int leftCnt = 0;

        //클래스 배열에 넣기
        for (int i = 0; i < placesTest.length; i++) {
            if (placesTest[i].getX() > avgX) {      //1,4사분면에 위치할 때
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("right");
                rightCnt++;
            } else if (placesTest[i].getX() < avgX) {       //2.3사분면에 위치할 때
                placesTest[i].setSlope((placesTest[i].getY() - avgY) / (placesTest[i].getX() - avgX));
                placesTest[i].setWhere("left");
                leftCnt++;
            }else if(placesTest[i].getX() == avgX){         //y죽에 위치할 때
                if (placesTest[i].getY() >= avgY) {         //점이 y축에 위치하면서 중심좌표보다 위에 있거나 같을 때
                    placesTest[i].setSlope(10000000);        //1,4사분면에 위치하는 배열에 넣음
                    placesTest[i].setWhere("right");
                    rightCnt++;
                } else if (placesTest[i].getY() < avgY) {       //점이 y축면에 위치하서 중심좌표보다 아래에 있을 때
                    placesTest[i].setSlope(10000000);        //2,3사분면에 위치하는 배열에 넣음
                    placesTest[i].setWhere("left");
                    leftCnt++;
                }
            }
        }

        PlacesTest SlopeInOneFour2[] = new PlacesTest[rightCnt];        //right 개수만큼 배열 만들기
        PlacesTest SlopeInTwoThree2[] = new PlacesTest[leftCnt];        //left 개수만큼 배열 만들기

        for (int i = 0; i < SlopeInOneFour2.length; i++) {           //초기화
            SlopeInOneFour2[i] = new PlacesTest();
        }

        for (int i = 0; i < SlopeInTwoThree2.length; i++) {           //초기화
            SlopeInTwoThree2[i] = new PlacesTest();
        }

        int j=0;
        int k=0;

        System.out.print("1,4사분면 배열 값 :");
        for (int i = 0; i < placesTest.length; i++) {
            if (placesTest[i].getWhere().equals("right")) {         //right일 때(1,4사분면)
                System.out.print("["+placesTest[i].getSlope()+"]");
                SlopeInOneFour2[j] = placesTest[i];      //right만 옮겨 담기
                j++;
            }
        }

        System.out.println();

        System.out.print("2,3사분면 배열 값 :");
        for (int i = 0; i < placesTest.length; i++) {
            if (placesTest[i].getWhere().equals("left")) {          //left일 때(2,3사분면)
                System.out.print("["+placesTest[i].getSlope()+"]");
                SlopeInTwoThree2[k] = placesTest[i];     //left만 옮겨 담기
                k++;
            }
        }

        System.out.println();


        //양수 -> 음수 크기 순으로 담기 1,4사분면
        for (int i = 0; i < SlopeInOneFour2.length; i++) {
            int maxid=i;
            double max = SlopeInOneFour2[i].getSlope();
            for (int n = i; n < SlopeInOneFour2.length; n++) {
                if (max < SlopeInOneFour2[n].getSlope()) {
                    max = SlopeInOneFour2[n].getSlope();
                    maxid = n;
                }
            }
            PlacesTest temp = SlopeInOneFour2[i];
            SlopeInOneFour2[i] = SlopeInOneFour2[maxid];
            SlopeInOneFour2[maxid] = temp;
        }

        //양수 -> 음수 크기 순으로 담기 2,3사분면
        for (int i = 0; i < SlopeInTwoThree2.length; i++) {
            int maxid=i;
            double max = SlopeInTwoThree2[i].getSlope();
            for (int n = i; n < SlopeInTwoThree2.length; n++) {
                if (max < SlopeInTwoThree2[n].getSlope()) {
                    max = SlopeInTwoThree2[n].getSlope();
                    maxid = n;
                }
            }
            PlacesTest temp = SlopeInTwoThree2[i];
            SlopeInTwoThree2[i] = SlopeInTwoThree2[maxid];
            SlopeInTwoThree2[maxid] = temp;
        }

        System.out.print("1,4사분면 최대 값 순서대로 배열값 :");
        for (int i = 0; i < SlopeInOneFour2.length; i++) {
            System.out.println("["+SlopeInOneFour2[i].toString()+"]");
        }

        System.out.println();

        System.out.print("2,3사분면 최대 값 순서대로 배열값 :");
        for (int i = 0; i < SlopeInTwoThree2.length; i++) {
            System.out.println("["+SlopeInTwoThree2[i].toString()+"]");
        }
        System.out.println();

        List<PlacesTest> List14 = Arrays.asList(SlopeInOneFour2);
        List<PlacesTest> List23 = Arrays.asList(SlopeInTwoThree2);

        List<PlacesTest> placesTests = new ArrayList<>();       //최종적으로 담을 리스트 선언

        placesTests.addAll(List14);
        placesTests.addAll(List23);

        List<PlacesTest> finalPlacesTest = divideDays(placesTests, dayStayAvg, days, count);     //날짜 나누어 담기

        int maxid = 0;
        System.out.println("마지막 날 다를 때 한칸 씩 앞으로 땡기기 테스트 전 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }


        while (finalPlacesTest.get(count - 1).getDays() != days) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 다를 때 실행 됏음");
            int sumStayDays[] = new int[finalPlacesTest.get(count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (int n = 0; n < finalPlacesTest.size(); n++) {
                    if (i + 1 == finalPlacesTest.get(n).getDays()) {
                        sumStayDays[i] += finalPlacesTest.get(n).getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
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

            if (maxid == finalPlacesTest.get(count - 1).getDays()) {
                finalPlacesTest.get(count - 1).setDays(finalPlacesTest.get(count - 2).getDays() + 1);    //마지막 데이의 스테이 합이 최대일 때 다음데이 넣고              //모든 데이가 일평균을 넘지 못할 수도 있음
                continue;                                                                               //반복문의 맨 처음으로
            }
            if (days == maxid) {                                                                        //마지막 days가 들어갔을 때
                break;                                                                                  //반복문 빠져나가기
            }

            int startid = 0;
            for (int i = 0; i < finalPlacesTest.size(); i++) {
                if (finalPlacesTest.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < finalPlacesTest.size(); i++) {
                if (i == count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    finalPlacesTest.get(i).setDays(finalPlacesTest.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }

        System.out.println("마지막 데이가 같을 때까지 넣은 후 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }

        while (finalPlacesTest.get(count - 1).getDays() == days) {                                                               //마지막날이 days랑 다를 때

            System.out.println("마지막 날 이랑 days랑 같을 때 실행 됏음");
            int sumStayDays[] = new int[finalPlacesTest.get(count - 1).getDays()];                                             //각 데이들의 스테이 합을 구하기 위해
            int maxStay = sumStayDays[0];
            for (int i = 0; i < sumStayDays.length; i++) {
                for (int n = 0; n < finalPlacesTest.size(); n++) {
                    if (i + 1 == finalPlacesTest.get(n).getDays()) {
                        sumStayDays[i] += finalPlacesTest.get(n).getStay();                 //days별로 스테이의 합을 구함 최대값을 구하기 위해
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

            if (sumStayDays[days-1] >= dayStayAvg/2) {                                                 //마지막 데이의 스테이 합이 평균 보다 1/2클 때
                break;                                                                                 //반복문 종료
            }


            int startid = 0;
            for (int i = 0; i < finalPlacesTest.size(); i++) {
                if (finalPlacesTest.get(i).getDays() == maxid) {
                    startid = i;                                                            //데이 줄일 거 시작할 부분
                }
            }
            System.out.println("데이 줄이기 시작할 인덱스 부분 : " + startid);
            for (int i = startid; i < finalPlacesTest.size(); i++) {
                if (i == count - 1) {                                                        //제일 마지막일 경우 리턴
                    break;
                } else {
                    finalPlacesTest.get(i).setDays(finalPlacesTest.get(i + 1).getDays());       //데이를 한칸씩 앞으로 땡김
                }
            }
        }


        System.out.println("데이즈 값 :" + days);
        System.out.println("마지막 테스트 : ");
        for (int i = 0; i < placesTests.size(); i++) {
            System.out.println("[" + finalPlacesTest.get(i).toString() + "]");
        }

        System.out.println("test good!!");

    }

/*
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

    }*/
/*
    @Test
    public void MapParsingDB() {
        String word = "강원";
        MapRepository mapRepository = null;
        List<Places> bodyJson = mapRepository.findByAddressContains(word);
        System.out.println("데베에서 뽑아온 Places값 :"+bodyJson.toString());
    }*/

}