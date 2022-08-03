package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {
	
	// No recomendable crearlo asi en produccion
	@Bean
	public NewTopic walletBcEvents() {
		return TopicBuilder.name("buy-bootcoin-events")
				.partitions(3)
				.replicas(3)
				.build();
	}
}
