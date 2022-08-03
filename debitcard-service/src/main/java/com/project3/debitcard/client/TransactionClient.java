package com.project3.debitcard.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project3.debitcard.entity.Transaction;

import reactor.core.publisher.Flux;

@Component
public class TransactionClient {
    private WebClient client = WebClient.create("http://transaction-service:9004/transaction");


    public Flux<Transaction> findAllWithDetail(){
        return client.get()
                .uri("/findAllWithDetail")
                .retrieve()
                .bodyToFlux(Transaction.class);
    };
}
