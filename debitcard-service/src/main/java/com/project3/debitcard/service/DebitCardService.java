package com.project3.debitcard.service;

import com.project3.debitcard.entity.DebitCard;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DebitCardService {
	
	Flux<DebitCard> findAll();

    Mono<DebitCard> create(DebitCard dc);

    Mono<DebitCard> findById(String id);

    Mono<DebitCard> update(DebitCard dc, String id);

    Mono<DebitCard> delete(String id);
	
    Flux<DebitCard> principalDebitAccount(String cardNumber);
    
	Flux<DebitCard> accountDetail(String cardNumber);
	
}
