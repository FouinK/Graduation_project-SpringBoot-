package com.example.VivaLaTrip.Controller;

import com.example.VivaLaTrip.Form.MapData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class MapController {

    @PostMapping
    public void mapRequest(@RequestBody MapData[] mapData){
        for (int i = 0; i < mapData.length; i++) {
            log.info("JSOM 값 확인"+mapData[i]);
        }
    }
}