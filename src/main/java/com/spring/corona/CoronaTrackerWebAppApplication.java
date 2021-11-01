package com.spring.corona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronaTrackerWebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaTrackerWebAppApplication.class, args);
	}

}
