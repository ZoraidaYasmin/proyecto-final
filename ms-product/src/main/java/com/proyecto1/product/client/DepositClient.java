package com.proyecto1.product.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Deposit;

import reactor.core.publisher.Flux;

@Component
public class DepositClient {
    private WebClient client = WebClient.create("http://deposit-service:9005/deposit");

    public Flux<Deposit> getDeposit(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Deposit.class);
    }
}
