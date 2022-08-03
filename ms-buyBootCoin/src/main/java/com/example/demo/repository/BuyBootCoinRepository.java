package com.example.demo.repository;

import com.example.demo.entity.BuyBootCoin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BuyBootCoinRepository extends ReactiveCrudRepository<BuyBootCoin, String> {
}
