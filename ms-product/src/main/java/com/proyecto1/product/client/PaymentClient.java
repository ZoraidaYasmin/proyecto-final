package com.proyecto1.product.client;


import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.product.entity.Payment;

import reactor.core.publisher.Flux;

@Component
public class PaymentClient {
    private WebClient client = WebClient.create("http://payment-service:9006/payment");

    public Flux<Payment> getPayment(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(Payment.class);
    }
}
