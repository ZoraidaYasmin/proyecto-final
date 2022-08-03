package com.proyecto1.product.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proyecto1.product.client.CustomerClient;
import com.proyecto1.product.client.DepositClient;
import com.proyecto1.product.client.PaymentClient;
import com.proyecto1.product.client.PurchaseClient;
import com.proyecto1.product.client.SignatoryClient;
import com.proyecto1.product.client.TransactionClient;
import com.proyecto1.product.client.WithDrawalClient;
import com.proyecto1.product.entity.Customer;
import com.proyecto1.product.entity.Deposit;
import com.proyecto1.product.entity.Payment;
import com.proyecto1.product.entity.Product;
import com.proyecto1.product.entity.Purchase;
import com.proyecto1.product.entity.Signatory;
import com.proyecto1.product.entity.Transaction;
import com.proyecto1.product.entity.Withdrawal;
import com.proyecto1.product.repository.ProductRepository;
import com.proyecto1.product.service.ProductService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductRepository productRepository;
    
    @Autowired
    TransactionClient transactionClient;
    
    @Autowired
    CustomerClient customerClient;
    
    @Autowired
    DepositClient depositClient;
    
    @Autowired
    WithDrawalClient withDrawalClient;

    @Autowired
    PaymentClient paymentClient;

    @Autowired
    PurchaseClient purchaseClient;

    @Autowired
    SignatoryClient signatoryClient;

    @Override
    public Flux<Product> findAll() {
        log.info("Method call FindAll - product");
        return productRepository.findAll();
    }

    @Override
    public Mono<Product> create(Product c) {
        log.info("Method call Create - product");
        return productRepository.save(c);
    }

    @Override
    public Mono<Product> findById(String id) {
        log.info("Method call findById - product");
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> update(Product c, String id) {
        log.info("Method call update - product");
        return productRepository.findById(id)
                .map( x -> {
                    x.setIndProduct(c.getIndProduct());
                    x.setDescIndProduct(c.getDescIndProduct());
                    x.setTypeProduct(c.getTypeProduct());
                    x.setDescTypeProduct(c.getDescTypeProduct());
                    x.setAmountPerDay(c.getAmountPerDay());
                    x.setAmountPerMonth(c.getAmountPerMonth());
                    return x;
                }).flatMap(productRepository::save);
    }

    @Override
    public Mono<Product> delete(String id) {
        log.info("Method call delete - product");
        return productRepository.findById(id).flatMap(
                x -> productRepository.delete(x).then(Mono.just(new Product())));
    }
    
    @Override
	public Flux<Transaction> reportByDate(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate, @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate) {
    	
    	
        return transactionClient.findAll()
        		.filter(trans -> (trans.getRegistrationDate().isAfter(startDate) && trans.getRegistrationDate().isBefore(endDate)))
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMapMany( customer -> {
                            return findById(trans.getProductId())
                                    .flatMapMany( product -> depositClient.getDeposit()
                                            .filter(x -> x.getTransactionId().equals(trans.getId()))
                                            .collectList()
                                            .flatMapMany((deposit -> {
                                                return withDrawalClient.getWithDrawal()
                                                        .filter(i -> i.getTransactionId().equals(trans.getId()))
                                                        .collectList()
                                                        .flatMapMany(( withdrawals -> {
                                                            return paymentClient.getPayment()
                                                                    .filter(z -> z.getTransactionId().equals(trans.getId()))
                                                                    .collectList()
                                                                    .flatMapMany((payments -> {
                                                                        return purchaseClient.getPurchase()
                                                                                .filter(y -> y.getTransactionId().equals(trans.getId()))
                                                                                .collectList()
                                                                                .flatMapMany(purchases -> {
                                                                                    return signatoryClient.getSignatory()
                                                                                            .filter(o -> o.getTransactionId().equals(trans.getId()))
                                                                                            .collectList()
                                                                                            .flatMapMany(signatories -> {
                                                                                                ValorAllValidator(trans, customer, product, deposit, withdrawals, payments, purchases, signatories);
                                                                                                return Flux.just(trans);
                                                                                            });
                                                                                });

                                                                    } ));
                                                        } ));
                                            })));
                        }));

	}

    private void ValorAllValidator(Transaction trans, Customer customer, Product product, List<Deposit> deposit, List<Withdrawal> withdrawals, List<Payment> payments, List<Purchase> purchases, List<Signatory> signatories) {
        trans.setCustomer(customer);
        trans.setProduct(product);
        trans.setDeposit(deposit.stream().collect(Collectors.toList()));
        trans.setWithdrawal(withdrawals.stream().collect(Collectors.toList()));
        trans.setPayments(payments.stream().collect(Collectors.toList()));
        trans.setPurchases(purchases.stream().collect(Collectors.toList()));
        trans.setSignatories(signatories.stream().collect(Collectors.toList()));
    }
}
