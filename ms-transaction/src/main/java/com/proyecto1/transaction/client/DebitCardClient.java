package com.proyecto1.transaction.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.DebitCard;
import com.proyecto1.transaction.entity.VirtualWalletEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class DebitCardClient {
	private WebClient client = WebClient.create("http://debitcard-service:8081/debitcard");

    public Flux<DebitCard> getDebitCards(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
    
    public Mono<DebitCard> getDebitCardByTransactionId(String id){
    	return client.get()
                .uri("/findByTransactionId/"+id)
                .retrieve()
                .bodyToMono(DebitCard.class);
    }
    
    public Flux<DebitCard> getPrincipalDebitAccount(String cardNumber){
        return client.get()
        		.uri(uriBuilder -> uriBuilder
                        .path("/principalDebitAccount/{id}")
                        .build(cardNumber)
                )
                .retrieve()
                .bodyToFlux(DebitCard.class);
    }
}
