package com.proyecto1.product.service;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proyecto1.product.entity.Product;
import com.proyecto1.product.entity.Transaction;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {

    Flux<Product> findAll();

    Mono<Product> create(Product c);

    Mono<Product> findById(String id);

    Mono<Product> update(Product c, String id);

    Mono<Product> delete(String id);

    Flux<Transaction> reportByDate(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate);
}
