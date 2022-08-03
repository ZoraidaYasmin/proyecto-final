package com.proyecto1.transaction.consumer;

import java.math.BigDecimal;

import com.proyecto1.transaction.client.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.proyecto1.transaction.entity.BuyBootCoin;
import com.proyecto1.transaction.entity.Transaction;
import com.proyecto1.transaction.entity.VirtualWalletEvent;
import com.proyecto1.transaction.service.TransactionService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class VirtualWalletConsumer {

    @Autowired
    DebitCardClient debitCardClient;

    @Autowired
    BuyBcClient buyBcClient;

    @Autowired
    WalletBcClient walletBcClient;

    @Autowired
    VirtualWalletClient virtualWalletClient;

    @Autowired
    TransactionService transactionService;

    @KafkaListener(topics = {"virtual-wallet-events"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) {
        log.info("ConsumerRecord: {}", consumerRecord);
        VirtualWalletEvent vwEvent = new Gson().fromJson(consumerRecord.value(), VirtualWalletEvent.class);
        if (vwEvent.getCardNumberEmisor() != null) {
            updateEmisor(vwEvent);
        }
        if (vwEvent.getCardNumberReceptor() != null) {
            updateReceptor(vwEvent);
        }


    }

    @KafkaListener(topics = {"exchange-events"})
    public void onMessageBuyBc(ConsumerRecord<Integer, String> consumerRecord) {
        log.info("ConsumerRecord: {}", consumerRecord);
        ExchangeEvent exchangeEvent = new Gson().fromJson(consumerRecord.value(), ExchangeEvent.class);
        BuyBootCoin buyBootCoin = exchangeEvent.getBuyBootCoin();

        Mono<BuyBootCoin> buyBootCoinUpdate = walletBcClient.getWalletBcById(buyBootCoin.getWalletId()).flatMap(wbc -> {
            wbc.getCelular();
            buyBootCoin.getModoDePago();

            if (buyBootCoin.getModoDePago().equalsIgnoreCase("CUENTA BANCARIA")) {
                transactionValidate(buyBootCoin).flatMap(aTrans -> {
                    if (aTrans) {
                        return updateReceptorAmount(buyBootCoin).collectList().flatMap(lstTrans -> {
                            return Mono.just(buyBootCoin);
                        });
                    }
                         return Mono.just(buyBootCoin);
                });

/*
                if (buyBootCoin.getModoDePago().equals("YANKI")) {
                    yankiValidate(buyBootCoin).flatMap(aYanki -> {

                        if (aYanki) {

                        }
                        return Mono.just(null);
                    });
                }*/

            }
            return Mono.just(null);
        });


        buyBootCoinUpdate.subscribe(t -> log.info("Entro a funcion updateReceptorBootcoin valor {}", t.toString()));

    }

    private void updateAmountBootCoin(String accountIdReceptor, BigDecimal monto) {
        transactionService.findById(accountIdReceptor).flatMap(t -> {
            t.setAvailableBalance(t.getAvailableBalance().subtract(monto));
            return Mono.just(t);
        });
    }

    private void updateEmisor(VirtualWalletEvent vwEvent) {


        Flux<Transaction> tra = debitCardClient.getPrincipalDebitAccount(vwEvent.getCardNumberEmisor()).flatMap(debitCard -> {
            if (debitCard.getTrans().getAvailableBalance().compareTo(vwEvent.getAmount()) >= 0) {
                debitCard.getTrans().setAvailableBalance(debitCard.getTrans().getAvailableBalance().subtract(vwEvent.getAmount()));
                return transactionService.update(debitCard.getTrans(), debitCard.getTrans().getId());
            } else {
                return Mono.error(new Throwable());
            }
        });

        tra.subscribe(t -> log.info("Entro a funcion updateEmisor valor {}", t.toString()));
    }

    private void updateReceptor(VirtualWalletEvent vwEvent) {
        Flux<Transaction> tra = debitCardClient.getPrincipalDebitAccount(vwEvent.getCardNumberReceptor()).flatMap(debitCard -> {
            debitCard.getTrans().setAvailableBalance(vwEvent.getAmount().add(debitCard.getTrans().getAvailableBalance()));
            return transactionService.update(debitCard.getTrans(), debitCard.getTrans().getId());
        });

        tra.subscribe(t -> log.info("Entro a funcion updateReceptor valor {}", t.toString()));
    }
    private Flux<Transaction> updateReceptorAmount(BuyBootCoin buyBootCoin) {
        return transactionService.findAllWithDetail()
                .filter(trans -> trans.getCustomerId().equalsIgnoreCase(buyBootCoin.getAccountIdReceptor()))
                .collectList()
                .flatMapMany(trans -> {
                    trans.sort((o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate()));
                    Transaction otrans = trans.stream().filter(t -> t.getProduct().getTypeProduct() == 1 || t.getProduct().getTypeProduct() == 2).findFirst().get();
                // seteamos valor en soles al vendedor
                    otrans.setAvailableBalance(otrans.getAvailableBalance().add(buyBootCoin.getMonto()));
                    log.info("Receptor monto listo para updatear");
                    return transactionService.update(otrans, otrans.getId());
                });
    }

    private Mono<Boolean> transactionValidate(BuyBootCoin buyBootCoin) {
        return transactionService.findAllWithDetail().filter(trans -> trans.getCustomerId().equalsIgnoreCase(buyBootCoin.getAccountIdReceptor()))
                .filter(trans -> trans.getProduct().getTypeProduct() == 1 || trans.getProduct().getTypeProduct() == 2)
                .hasElements();
    }

    private Mono<Boolean> yankiValidate(BuyBootCoin buyBootCoin) {
        return walletBcClient.getWalletBcById(buyBootCoin.getWalletId()).flatMap(walletBc -> {
            return virtualWalletClient.getVirtualWallets().filter(vw -> vw.getCellphone().equalsIgnoreCase(walletBc.getCelular()))
                    .hasElements();
        });
    }


}
