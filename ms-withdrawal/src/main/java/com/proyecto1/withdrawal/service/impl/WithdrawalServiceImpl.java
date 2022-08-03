package com.proyecto1.withdrawal.service.impl;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto1.withdrawal.client.DebitCardClient;
import com.proyecto1.withdrawal.client.TransactionClient;
import com.proyecto1.withdrawal.entity.Deposit;
import com.proyecto1.withdrawal.entity.Transaction;
import com.proyecto1.withdrawal.entity.Withdrawal;
import com.proyecto1.withdrawal.repository.WithdrawalRepository;
import com.proyecto1.withdrawal.service.WithdrawalService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private static final Logger log = LogManager.getLogger(WithdrawalServiceImpl.class);

    @Autowired
    WithdrawalRepository withdrawalRepository;

    @Autowired
    TransactionClient transactionClient;
    
    @Autowired
    DebitCardClient debitCardClient;
    
    @Override
    public Flux<Withdrawal> findAll() {
        log.info("Method call FindAll - withdrawal");
        return withdrawalRepository.findAll();
    }

    @Override
    public Mono<Withdrawal> create(Withdrawal c) {
        log.info("Method call Create - withdrawal");
        return transactionClient.getTransactionWithDetails(c.getTransactionId())
                .filter( x -> x.getProduct().getIndProduct() == 2)
                .hasElement()
                .flatMap( y -> {
                    if(y){
                    	return transactionClient.getTransactionWithDetails(c.getTransactionId()).flatMap(account -> {
                    		if(account.getMaxAmountTransaction() > account.getCurrentNumberTransaction()) {
                    			return updateCurrentNumberTransaction(transactionClient.getTransactionWithDetails(c.getTransactionId()))
                    					.flatMap(trans -> {
                    						return validateDebitCard(c);
                    					});
                    			// return depositRepository.save(deposit).doOnNext(depositSaved -> updateCurrentNumberTransaction(transactionClient.getTransactionWithDetails(deposit.getTransactionId())));
                    		} else {
                    			// Maximo Numero de trasacciones, se cobra comision
                    			c.setWithdrawalAmount(c.getWithdrawalAmount().add(account.getMaintenanceCommission().multiply(c.getWithdrawalAmount().divide(BigDecimal.valueOf(100)))));
                    			return validateDebitCard(c);
                    		}
                    	});
                        
                    }else{
                        return Mono.error(new RuntimeException("The account entered is not a bank account"));
                    }
                });

    }

    @Override
    public Mono<Withdrawal> findById(String id) {
        log.info("Method call FindById - withdrawal");
        return withdrawalRepository.findById(id);
    }

    @Override
    public Mono<Withdrawal> update(Withdrawal c, String id) {
        log.info("Method call Update - withdrawal");
        return withdrawalRepository.findById(id)
                .map( x -> {
                    x.setDate(c.getDate());
                    x.setWithdrawalAmount(c.getWithdrawalAmount());
                    x.setDescription(c.getDescription());
                    return x;
                }).flatMap(withdrawalRepository::save);
    }

    @Override
    public Mono<Withdrawal> delete(String id) {
        log.info("Method call Delete - withdrawal");
        return withdrawalRepository.findById(id).flatMap( x -> withdrawalRepository.delete(x).then(Mono.just(new Withdrawal())));
    }
    
    public Mono<Transaction> updateCurrentNumberTransaction(Mono<Transaction> trans) {
    	return trans.flatMap(t -> {
    		t.setCurrentNumberTransaction(t.getCurrentNumberTransaction()+1);
    		return transactionClient.updateTransaction(t);
    	});
    }
    
    private Mono<Withdrawal> validateDebitCard(Withdrawal withdrawal){
    	return transactionClient.getTransactionWithDetails(withdrawal.getTransactionId())
    			.flatMap(trans -> debitCardClient.getAccountDetailByDebitCard(trans.getCardNumber())
    					.collectList()
    					.flatMap(dc -> {
    						Transaction otrans = dc.stream().findFirst().get().getTransaction().stream().filter(t -> t.getProduct().getIndProduct() == 2 && t.getAvailableBalance().compareTo(withdrawal.getWithdrawalAmount()) >= 0).findFirst().get();
    						if (otrans != null) {
    							withdrawal.setTransactionId(otrans.getId());
        						return withdrawalRepository.save(withdrawal);
    						}else {
    							return Mono.error(new RuntimeException("No hay cuentas con sado disponible"));
    						}
    					}));
    }
}
