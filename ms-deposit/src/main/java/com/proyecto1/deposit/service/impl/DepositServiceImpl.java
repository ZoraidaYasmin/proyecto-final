package com.proyecto1.deposit.service.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto1.deposit.client.DebitCardClient;
import com.proyecto1.deposit.client.TransactionClient;
import com.proyecto1.deposit.dto.DepositDTO;
import com.proyecto1.deposit.entity.Deposit;
import com.proyecto1.deposit.entity.Transaction;
import com.proyecto1.deposit.repository.DepositRepository;
import com.proyecto1.deposit.service.DepositService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class DepositServiceImpl implements DepositService {

    private static final Logger log = LogManager.getLogger(DepositServiceImpl.class);
    @Autowired
    DepositRepository depositRepository;

    @Autowired
    TransactionClient transactionClient;
    
    @Autowired
    DebitCardClient debitCardClient;

    @Override
    public Flux<Deposit> findAll() {
        log.info("Method call FindAll - deposit");
        return depositRepository.findAll();
    }

    @Override
    public Mono<Deposit> create(DepositDTO c) {
        log.info("Method call create - deposit");
        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(c,deposit);
        return transactionClient.getTransactionWithDetails(c.getTransactionId())
                .filter( x -> x.getProduct().getIndProduct() == 2)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                    	return transactionClient.getTransactionWithDetails(deposit.getTransactionId()).flatMap(account -> {
                    		if(account.getMaxAmountTransaction() > account.getCurrentNumberTransaction()) {
                    			return updateCurrentNumberTransaction(transactionClient.getTransactionWithDetails(deposit.getTransactionId()))
                    					.flatMap(trans -> {
                    						return validateDebitCard(deposit);
                    					});
                    			// return depositRepository.save(deposit).doOnNext(depositSaved -> updateCurrentNumberTransaction(transactionClient.getTransactionWithDetails(deposit.getTransactionId())));
                    		} else {
                    			// Maximo Numero de trasacciones, se cobra comision
                    			deposit.setDepositAmount(deposit.getDepositAmount().add(account.getMaintenanceCommission().multiply(deposit.getDepositAmount().divide(BigDecimal.valueOf(100)))));
                    			return validateDebitCard(deposit);
                    		}
                    	});
                        //return depositRepository.save(deposit);
                    }else{
                        return Mono.error(new RuntimeException("The account entered is not a bank account"));
                    }
                });
    }
    
    private Mono<Deposit> validateDebitCard(Deposit deposit){
    	return transactionClient.getTransactionWithDetails(deposit.getTransactionId())
    			.flatMap(trans -> debitCardClient.getAccountDetailByDebitCard(trans.getCardNumber())
    					/*
    					.filter(dc -> dc.getTransaction().get(0).getProduct() != null)
    					.filter(dc -> dc.getProduct().getIndProduct() == 2)
    					.filter(dc -> trans.getAvailableBalance().compareTo(deposit.getDepositAmount()) >= 0)
    					*/
    					.collectList()
    					.flatMap(dc -> {
    						Transaction otrans = dc.stream().findFirst().get().getTransaction().stream().filter(t -> t.getProduct().getIndProduct() == 2 && t.getAvailableBalance().compareTo(deposit.getDepositAmount()) >= 0).findFirst().get();
    						if (otrans != null) {
    							deposit.setTransactionId(otrans.getId());
        						return depositRepository.save(deposit);
    						}else {
    							return Mono.error(new RuntimeException("No hay cuentas con sado disponible"));
    						}
    					}));
    }

    public Mono<Transaction> updateCurrentNumberTransaction(Mono<Transaction> trans) {
    	return trans.flatMap(t -> {
    		t.setCurrentNumberTransaction(t.getCurrentNumberTransaction()+1);
    		return transactionClient.updateTransaction(t);
    	});
    }
    
    @Override
    public Mono<Deposit> findById(String id) {
        log.info("Method call findById - deposit");
        return depositRepository.findById(id);
    }

    @Override
    public Mono<Deposit> update(DepositDTO c, String id) {
        log.info("Method call update - deposit");
        Deposit deposit = new Deposit();
        BeanUtils.copyProperties(c,deposit);
        return depositRepository.findById(id)
                .map( x -> {
                    x.setDate(c.getDate());
                    x.setDepositAmount(c.getDepositAmount());
                    x.setDescription(c.getDescription());
                    return x;
                }).flatMap(depositRepository::save);
    }

    @Override
    public Mono<Deposit> delete(String id) {
        log.info("Method call delete - deposit");
        return depositRepository.findById(id).flatMap( x -> depositRepository.delete(x).then(Mono.just(new Deposit())));
    }
}
