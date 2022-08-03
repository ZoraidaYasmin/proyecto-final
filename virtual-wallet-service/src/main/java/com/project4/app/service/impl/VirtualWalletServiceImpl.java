package com.project4.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Service;

import com.project4.app.config.CacheConfig;
import com.project4.app.entity.VirtualWallet;
import com.project4.app.repository.VirtualWalletRepository;
import com.project4.app.service.VirtualWalletService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(name = "cache.enabled", havingValue = "true")
public class VirtualWalletServiceImpl implements VirtualWalletService {

	@Autowired
	VirtualWalletRepository vwRepository;

	@Autowired
	private ReactiveHashOperations<String, String, VirtualWallet> hashOperations;
	
	private static final String KEY = "virtual-wallet-cache";
	
	@Override
	public Flux<VirtualWallet> findAll() {
		return vwRepository.findAll();
	}

	@Override
	public Mono<VirtualWallet> create(VirtualWallet virtualWallet) {
		return vwRepository.save(virtualWallet);
	}

	@Override
	//@Cacheable(cacheNames = CacheConfig.WALLET_CACHE, unless = "#result == null")
	public Mono<VirtualWallet> findById(String id) {
		return hashOperations.get(KEY, id)
                .switchIfEmpty(this.getFromDatabaseAndCache(id));
	}
	
	private Mono<VirtualWallet> getFromDatabaseAndCache(String id) {
        return vwRepository.findById(id).flatMap(dto -> this.hashOperations.put(KEY, id, dto)
                                                       .thenReturn(dto));
    }

	@Override
	@CachePut(cacheNames = CacheConfig.WALLET_CACHE, key = "#id", unless = "#result == null")
	public Mono<VirtualWallet> update(VirtualWallet virtualWallet, String id) {
        return vwRepository.findById(id)
                .map( x -> {
                    x.setAmount(x.getAmount());
                    x.setCardNumberEmisor(x.getCardNumberEmisor());
                    x.setCardNumberReceptor(x.getCardNumberReceptor());
                    x.setCellphone(x.getCellphone());
                    x.setDni(x.getDni());
                    x.setOperation(x.getOperation());
                    return x;
                }).flatMap(vwRepository::save);
	}

	@Override
	@CacheEvict(cacheNames = CacheConfig.WALLET_CACHE, key = "#id")
	public Mono<VirtualWallet> delete(String id) {
        return vwRepository.findById(id).flatMap( x -> vwRepository.delete(x).then(Mono.just(VirtualWallet.builder().id(id).build())));
	}
	
}
