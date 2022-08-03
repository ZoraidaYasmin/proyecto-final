package com.proyecto1.customer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto1.customer.client.DepositClient;
import com.proyecto1.customer.client.PaymentClient;
import com.proyecto1.customer.client.ProductClient;
import com.proyecto1.customer.client.PurchaseClient;
import com.proyecto1.customer.client.SignatoryClient;
import com.proyecto1.customer.client.TransactionClient;
import com.proyecto1.customer.client.WithDrawalClient;
import com.proyecto1.customer.dto.CustomerDTO;
import com.proyecto1.customer.entity.Customer;
import com.proyecto1.customer.entity.Deposit;
import com.proyecto1.customer.entity.Payment;
import com.proyecto1.customer.entity.Product;
import com.proyecto1.customer.entity.Purchase;
import com.proyecto1.customer.entity.Signatory;
import com.proyecto1.customer.entity.Transaction;
import com.proyecto1.customer.entity.Withdrawal;
import com.proyecto1.customer.repository.CustomerRepository;
import com.proyecto1.customer.service.CustomerService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LogManager.getLogger(CustomerServiceImpl.class);
    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    TransactionClient transactionClient;
    
    @Autowired
    ProductClient productClient;
    
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
    public Flux<Customer> findAll() {
        log.info("Method call FindAll - customer");
        return customerRepository.findAll();
    }

    @Override
    public Mono<Customer> create(CustomerDTO c) {
        log.info("Method call Create - customer");
       Customer customer = new Customer();
        BeanUtils.copyProperties(c,customer);
        return customerRepository.save(customer);
    }

    @Override
    public Mono<Customer> findById(String id) {
        log.info("Method call FindById - customer");
        return customerRepository.findById(id);
    }

    @Override
    public Mono<Customer> update(CustomerDTO c, String id) {
        log.info("Method call Update - customer");
        Customer customer = new Customer();
        BeanUtils.copyProperties(c,customer);

        return customerRepository.findById(id)
                .map( x -> {
                    x.setName(c.getName());
                    x.setLastName(c.getLastName());
                    x.setDocNumber(c.getDocNumber());
                    x.setTypeCustomer(c.getTypeCustomer());
                    x.setDescTypeCustomer(c.getDescTypeCustomer());
                    return x;
                }).flatMap(customerRepository::save);
    }

    @Override
    public Mono<Customer> delete(String id) {
        log.info("Method call Delete - customer");
        return customerRepository.findById(id).flatMap(
                x -> customerRepository.delete(x).then(Mono.just(new Customer())));
    }
    
    @Override
    public Flux<Transaction> summaryCustomerByProduct() {
    	
    	return transactionClient.findAll()
    			.flatMap(trans -> findById(trans.getCustomerId())
    					.flatMapMany(customer -> {
		                    return productClient.getProduct(trans.getProductId())
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
    
    private void calculateAverageDailyBalancesForMonth (Product product) {
    	
    }
}
