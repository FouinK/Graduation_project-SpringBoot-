package com.example.VivaLaTrip;

import com.example.VivaLaTrip.Entity.Places;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Arrays;
import java.util.Collections;

@EnableJpaAuditing//Jpa 활성화
@SpringBootApplication
public class VivaLaTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(VivaLaTripApplication.class, args);
	}

}