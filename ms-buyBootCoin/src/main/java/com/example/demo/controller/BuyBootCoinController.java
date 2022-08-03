package com.example.demo.controller;

import com.example.demo.entity.BuyBootCoin;
import com.example.demo.service.BuyBootCoinService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/buyBootCoin")
public class BuyBootCoinController {

    private static final Logger log = LogManager.getLogger(BuyBootCoinController.class);
    @Autowired
    private BuyBootCoinService buyBootCoinService;

    @GetMapping("/findAll")
    public Flux<BuyBootCoin> getBuyBootCoinAll(){
        log.info("Service call findAll - buyBootCoin");
        return buyBootCoinService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<BuyBootCoin> getBuyBootCoinId(@PathVariable String id){
        log.info("Service call findById - buyBootCoin");
        return buyBootCoinService.findById(id);
    }

    @PostMapping("/create")
    public Mono<BuyBootCoin> createBuyBootCoin(@RequestBody BuyBootCoin c){
        log.info("Service call create - buyBootCoin");
        return buyBootCoinService.create(c);
    }

    @PutMapping("/update/{id}")
    public Mono<BuyBootCoin> updateBuyBootCoin(@RequestBody BuyBootCoin c, @PathVariable String id){
        log.info("Service call update - buyBootCoin");
        return buyBootCoinService.update(c,id);
    }
    
    @PutMapping("/venderBc/{id}/{customerReceptorId}")
    public Mono<BuyBootCoin> venderBc(@PathVariable String id, @PathVariable String customerReceptorId){
        log.info("Service call update - buyBootCoin");
        return buyBootCoinService.venderBc(id, customerReceptorId);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<BuyBootCoin> deleteBuyBootCoin(@PathVariable String id){
        log.info("Service call delete - buyBootCoin");
        return buyBootCoinService.delete(id);
    }
}
