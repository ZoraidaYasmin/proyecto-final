package com.example.demo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.example.demo.entity.WalletBc;

public interface WalletBcRepository extends ReactiveCrudRepository<WalletBc, String> {
	
}
