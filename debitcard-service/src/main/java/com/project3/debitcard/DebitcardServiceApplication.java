package com.project3.debitcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DebitcardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DebitcardServiceApplication.class, args);
	}

}
