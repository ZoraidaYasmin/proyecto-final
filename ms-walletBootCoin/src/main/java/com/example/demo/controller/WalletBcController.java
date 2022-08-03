package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.WalletBc;
import com.example.demo.service.WalletBcService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/walletBc")
public class WalletBcController {
	@Autowired
    private WalletBcService walletBcService;

    @GetMapping("/findAll")
    public Flux<WalletBc> getPayments(){
        log.info("Service call FindAll - payment");
        return walletBcService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<WalletBc> getPayment(@PathVariable String id){
        log.info("Service call FindById - payment");
        return walletBcService.findById(id);
    }

    @PostMapping("/create")
    public Mono<WalletBc> createPayment(@RequestBody WalletBc c){
        log.info("Service call create - payment");
        return walletBcService.create(c);
    }

    @PutMapping("/update/{id}")
    public Mono<WalletBc> updatePayment(@RequestBody WalletBc c, @PathVariable String id){
        log.info("Service call update - payment");
        return walletBcService.update(c,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<WalletBc> deletePayment(@PathVariable String id){
        log.info("Service call delete - payment");
                return walletBcService.delete(id);
    }
}
