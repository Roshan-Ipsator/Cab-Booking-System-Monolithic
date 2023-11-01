package com.cabbookingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CabBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CabBookingSystemApplication.class, args);
	}

}
