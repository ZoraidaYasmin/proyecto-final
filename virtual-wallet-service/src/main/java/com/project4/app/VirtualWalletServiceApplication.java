package com.project4.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class VirtualWalletServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VirtualWalletServiceApplication.class, args);
	}

}
