package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Config.RestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.net.URISyntaxException;


@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<?> handler(RestException e) throws URISyntaxException {
        System.out.println("컨트롤러 어드바이스 출력");
        System.out.println("컨트롤러 어드바이스 상태 값 : " + e.getStatus());
        System.out.println("컨트롤러 어드바이스 메세지 값 : " + e.getMessage());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/"));

        return new ResponseEntity<>(httpHeaders, HttpStatus.MOVED_PERMANENTLY);
    }
}