package com.proyecto1.transaction.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.proyecto1.transaction.entity.WalletBc;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class WalletBcClient {
	
	private WebClient client = WebClient.create("http://wallet-bootcoin-service:8084/walletBc");

    public Flux<WalletBc> getWalletBcs(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(WalletBc.class);
    }
    
    public Mono<WalletBc> getWalletBcById(String id){
    	return client.get()
                .uri("/find/"+id)
                .retrieve()
                .bodyToMono(WalletBc.class);
    }
    
 
}
