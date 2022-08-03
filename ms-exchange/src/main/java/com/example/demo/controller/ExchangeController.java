package com.example.demo.controller;

import com.example.demo.entity.Exchange;
import com.example.demo.service.ExchangeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private static final Logger log = LogManager.getLogger(ExchangeController.class);
    @Autowired
    private ExchangeService exchangeService;

    @GetMapping("/findAll")
    public Flux<Exchange> getCustomers(){
        log.info("Service call findAll - Exchange");
        return exchangeService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<Exchange> getCustomer(@PathVariable String id){
        log.info("Service call findById - Exchange");
        return exchangeService.findById(id);
    }

    @PostMapping("/create")
    public Mono<Exchange> createCustomer(@RequestBody Exchange c){
        log.info("Service call create - Exchange");
        return exchangeService.create(c);
    }

    @PutMapping("/update/{id}")
    public Mono<Exchange> updateCustomer(@RequestBody Exchange c, @PathVariable String id){
        log.info("Service call update - Exchange");
        return exchangeService.update(c,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Exchange> deleteCustomer(@PathVariable String id){
        log.info("Service call delete - Exchange");
        return exchangeService.delete(id);
    }
}
