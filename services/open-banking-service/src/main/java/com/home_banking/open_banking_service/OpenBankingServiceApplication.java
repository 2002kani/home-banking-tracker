package com.home_banking.open_banking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class OpenBankingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenBankingServiceApplication.class, args);
	}

}
