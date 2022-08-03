package com.proyecto1.customer.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.customer.entity.Signatory;

import reactor.core.publisher.Flux;
@Component
public class SignatoryClient {
    private WebClient client = WebClient.create("http://signatory-service:9008/signatory");

    public Flux<Signatory> getSignatory(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Signatory.class);
    }
}
