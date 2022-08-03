package com.proyecto1.product.client;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Withdrawal;

import reactor.core.publisher.Flux;

@Component
public class WithDrawalClient {
    private WebClient client = WebClient.create("http://withdrawal-service:9009/withdrawal");

    public Flux<Withdrawal> getWithDrawal(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Withdrawal.class);
    }
}
