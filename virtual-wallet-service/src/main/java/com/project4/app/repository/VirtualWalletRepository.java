package com.project4.app.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.project4.app.entity.VirtualWallet;

public interface VirtualWalletRepository extends ReactiveCrudRepository<VirtualWallet, String> {

}
