package com.project4.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project4.app.entity.VirtualWallet;
import com.project4.app.entity.VirtualWalletEvent;
import com.project4.app.producer.VirtualWalletProducer;
import com.project4.app.service.VirtualWalletService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/virtualWallet")
public class VirtualWalletController {
	
	@Autowired
    private VirtualWalletService vwService;
	
	@Autowired
    private VirtualWalletProducer vwProducer;

    @GetMapping("/findAll")
    public Flux<VirtualWallet> getVirtualWallets(){
        return vwService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<VirtualWallet> getVirtualWallet(@PathVariable String id){
        return vwService.findById(id);
    }

    @PostMapping("/create")
    public Mono<VirtualWallet> createVirtualWallet(@RequestBody VirtualWallet vw){
        return vwService.create(vw);
    }

    @PutMapping("/update/{id}")
    public Mono<VirtualWallet> updateVirtualWallet(@RequestBody VirtualWallet vw, @PathVariable String id){
        return vwService.update(vw,id);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<VirtualWallet> deleteVirtualWallet(@PathVariable String id){
        return vwService.delete(id);
    }

    @PostMapping("/pay")
    public ResponseEntity<VirtualWalletEvent> payVirtualWallet(@RequestBody VirtualWalletEvent vw) throws JsonProcessingException{
    	if (vw.getCardNumberEmisor() != null || vw.getCardNumberReceptor() != null) {
    		vwProducer.sendVirtualWalletEvent(vw);
    	}else {
    		VirtualWallet vwT = new VirtualWallet();
    		vwT.setAmount(vw.getAmount());
    		vwT.setCellphone(vw.getCellphone());
    		vwT.setDni(vw.getDni());
    		vwT.setOperation(vw.getOperation());
    		vwService.create(vwT);
    	}
    	
        return ResponseEntity.status(HttpStatus.CREATED).body(vw);
    }
}
