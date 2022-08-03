package com.example.demo.service;

import com.example.demo.entity.WalletBc;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletBcService {

    Flux<WalletBc> findAll();

    Mono<WalletBc> findById(String id);

    Mono<WalletBc> update(WalletBc t, String id);

    Mono<WalletBc> delete(String id);

	Mono<WalletBc> create(WalletBc walletBc);
}
