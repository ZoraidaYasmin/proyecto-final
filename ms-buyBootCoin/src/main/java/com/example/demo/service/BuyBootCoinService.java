package com.example.demo.service;

import com.example.demo.entity.BuyBootCoin;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BuyBootCoinService {

    Flux<BuyBootCoin> findAll();

    Mono<BuyBootCoin> create(BuyBootCoin c);

    Mono<BuyBootCoin> findById(String id);

    Mono<BuyBootCoin> update(BuyBootCoin c, String id);

    Mono<BuyBootCoin> delete(String id);
	
	Mono<BuyBootCoin> venderBc(String id, String idReceptor);
}
