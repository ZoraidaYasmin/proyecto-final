package com.project3.debitcard.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.project3.debitcard.entity.DebitCard;

import reactor.core.publisher.Mono;

public interface DebitCardRepository extends ReactiveCrudRepository<DebitCard, String> {

	Mono<DebitCard> findByTransactionId(String id);
	
	Mono<DebitCard> findByCardNumber(String cardNumber);

}
