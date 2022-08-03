package com.proyecto1.transaction.client;

import com.proyecto1.transaction.entity.VirtualWallet;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Component
public class VirtualWalletClient {
    private WebClient client = WebClient.create("http://virtual-wallet-service:9013/virtualWallet");

    public Flux<VirtualWallet> getVirtualWallets(){
        return client.get()
                .uri("/findAll")
                .retrieve()
                .bodyToFlux(VirtualWallet.class);
    }

    public Mono<VirtualWallet> getVirtualWalletById(String id){
        return client.get()
                .uri("/find/"+id)
                .retrieve()
                .bodyToMono(VirtualWallet.class);
    }

    public Mono<VirtualWallet> updateVirtualWallet(VirtualWallet virtualWallet){
        return client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/update/{id}")
                        .build(virtualWallet.getId())
                )
                .bodyValue(virtualWallet)
                .retrieve()
                .bodyToMono(VirtualWallet.class);
    };
}
