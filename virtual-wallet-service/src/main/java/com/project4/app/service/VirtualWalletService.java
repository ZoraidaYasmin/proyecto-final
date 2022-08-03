package com.project4.app.service;

import com.project4.app.entity.VirtualWallet;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VirtualWalletService {
	
	Flux<VirtualWallet> findAll();

    Mono<VirtualWallet> create(VirtualWallet virtualWallet);

    Mono<VirtualWallet> findById(String id);

    Mono<VirtualWallet> update(VirtualWallet virtualWallet, String id);

    Mono<VirtualWallet> delete(String id);
}
