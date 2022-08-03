package com.proyecto1.customer.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.customer.entity.Purchase;

import reactor.core.publisher.Flux;

@Component
public class PurchaseClient {
    private WebClient client = WebClient.create("http://purchase-service:9007/purchase");

    public Flux<Purchase> getPurchase(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Purchase.class);
    }
}
