package com.example.VivaLaTrip.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping(value = "/error")
    public ResponseEntity<?> handleError(HttpServletRequest request) throws URISyntaxException {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        System.out.println("스테이터스 코드 : "+status.toString());
        System.out.println("에러 상태 작동 확인");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/"));
        if(status != null){
            int statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ResponseEntity<>(httpHeaders,HttpStatus.MOVED_PERMANENTLY);
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return new ResponseEntity<>(httpHeaders,HttpStatus.MOVED_PERMANENTLY);
            } else {
                return new ResponseEntity<>(httpHeaders,HttpStatus.MOVED_PERMANENTLY);
            }
        }
        return new ResponseEntity<>(httpHeaders,HttpStatus.MOVED_PERMANENTLY);
    }
}