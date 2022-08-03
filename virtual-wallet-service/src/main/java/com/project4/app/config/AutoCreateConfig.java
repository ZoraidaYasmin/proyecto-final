package com.project4.app.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AutoCreateConfig {
	
	// No recomendable crearlo asi en produccion
	@Bean
	public NewTopic virtualWalletEvents() {
		return TopicBuilder.name("virtual-wallet-events")
				.partitions(3)
				.replicas(3)
				.build();
	}
}
