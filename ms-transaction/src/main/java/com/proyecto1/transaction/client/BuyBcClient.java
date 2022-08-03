package com.proyecto1.transaction.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.BuyBootCoin;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class BuyBcClient {
	private WebClient client = WebClient.create("http://buyBootCoin-service:9015/buyBootCoin");

    public Flux<BuyBootCoin> getBuyBootcoins(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(BuyBootCoin.class);
    }
    
    public Mono<BuyBootCoin> getWalletBcById(String id){
    	return client.get()
                .uri("/find/"+id)
                .retrieve()
                .bodyToMono(BuyBootCoin.class);
    }
    
    public Mono<BuyBootCoin> updateBuyBootCoin(BuyBootCoin buyBootCoin){
        return client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/update/{id}")
                        .build(buyBootCoin.getId())
                )
                .bodyValue(buyBootCoin)
                .retrieve()
                .bodyToMono(BuyBootCoin.class);
    };
}
