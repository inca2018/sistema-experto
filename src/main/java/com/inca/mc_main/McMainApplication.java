package com.inca.mc_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class McMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(McMainApplication.class, args);
	}

}
