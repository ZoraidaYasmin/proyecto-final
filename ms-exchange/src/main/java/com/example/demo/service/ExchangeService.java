package com.example.demo.service;

import com.example.demo.entity.Exchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeService {

    Flux<Exchange> findAll();

    Mono<Exchange> create(Exchange c);

    Mono<Exchange> findById(String id);

    Mono<Exchange> update(Exchange c, String id);

    Mono<Exchange> delete(String id);

}
