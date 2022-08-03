package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.WalletBc;
import com.example.demo.repository.WalletBcRepository;
import com.example.demo.service.WalletBcService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WalletBcServiceImpl implements WalletBcService {

    @Autowired
    WalletBcRepository walletBcRepository;

	@Override
	public Flux<WalletBc> findAll() {
		return walletBcRepository.findAll();
	}

	@Override
	public Mono<WalletBc> findById(String id) {
		return walletBcRepository.findById(id);
	}

	@Override
	public Mono<WalletBc> update(WalletBc t, String id) {
		return walletBcRepository.findById(id)
                .map( x -> {
                    x.setCelular(t.getCelular());
                    x.setCelular(t.getCorreoElectronico());
                    x.setCustomerId(t.getCustomerId());
                    x.setMontoDeBcTotal(t.getMontoDeBcTotal());
                    x.setNumeroDocumento(t.getNumeroDocumento());
                    return x;
                }).flatMap(walletBcRepository::save);
	}

	@Override
	public Mono<WalletBc> delete(String id) {
		return walletBcRepository.findById(id).flatMap(
                x -> walletBcRepository.delete(x).then(Mono.just(new WalletBc())));
	}

	@Override
	public Mono<WalletBc> create(WalletBc walletBc) {
		return walletBcRepository.save(walletBc);
	}

}
