package com.proyecto1.deposit.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.deposit.entity.DebitCard;

import reactor.core.publisher.Flux;

@Component
public class DebitCardClient {
    private WebClient client = WebClient.create("http://debitcard-service:8081/debitcard");

    public Flux<DebitCard> getAccountDetailByDebitCard(String id){
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/accountDetail/{id}")
                        .build(id)
                )
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
