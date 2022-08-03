package com.proyecto1.transaction.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto1.transaction.client.CustomerClient;
import com.proyecto1.transaction.client.DepositClient;
import com.proyecto1.transaction.client.PaymentClient;
import com.proyecto1.transaction.client.ProductClient;
import com.proyecto1.transaction.client.PurchaseClient;
import com.proyecto1.transaction.client.SignatoryClient;
import com.proyecto1.transaction.client.WithDrawalClient;
import com.proyecto1.transaction.entity.Customer;
import com.proyecto1.transaction.entity.DateInterface;
import com.proyecto1.transaction.entity.Deposit;
import com.proyecto1.transaction.entity.Payment;
import com.proyecto1.transaction.entity.Product;
import com.proyecto1.transaction.entity.Purchase;
import com.proyecto1.transaction.entity.Signatory;
import com.proyecto1.transaction.entity.Transaction;
import com.proyecto1.transaction.entity.Withdrawal;
import com.proyecto1.transaction.repository.TransactionRepository;
import com.proyecto1.transaction.service.TransactionService;

import lombok.extern.java.Log;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransacionServiceImpl implements TransactionService {

    private static final Logger log = LogManager.getLogger(TransacionServiceImpl.class);
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CustomerClient customerClient;
    
    @Autowired
    ProductClient product;

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
    public Flux<Transaction> findAll() {
        log.info("Method call FindAll - transaction");
        return transactionRepository.findAll();
    }

    @Override
    public Mono<Transaction> save(Transaction t) {
        log.info("Method call Create - transaction");

        return this.findAllWithDetail()
                .filter( x -> x.getCustomerId().equals(t.getCustomerId())) // Buscamos el customerId de la lista
                .filter( x -> (x.getProduct().getIndProduct() == 2 && x.getCustomer().getTypeCustomer() == 1) && (x.getProduct().getId().equals(t.getProductId())) ) // Buscamos si tiene una cuenta bancaria y es cliente personal
                .hasElements()
                .flatMap( v -> {
                    if (v){
                        return Mono.error(new RuntimeException("The personal client cannot have more than one bank account"));
                    }else{
                        return this.findAllWithDetail()
                                .filter( x -> x.getCustomerId().equals(t.getCustomerId())) // Buscamos el customerId de la lista
                                .filter( x -> (x.getProduct().getIndProduct() == 1 && x.getCustomer().getTypeCustomer() == 1) && (x.getProduct().getId().equals(t.getProductId())) ) // Buscamos si tiene un credito y es cliente personal
                                .hasElements()
                                .flatMap( w -> {
                                   if (w){
                                       return Mono.error(new RuntimeException("The personal client cannot have more than one credit"));
                                   }else{
                                       return product.getProduct(t.getProductId())
                                               .filter( x -> (x.getIndProduct() == 2))
                                               .filter( x -> (x.getTypeProduct() == 1 || x.getTypeProduct() == 3) )
                                               .hasElement()
                                               .flatMap( zz -> {
                                                   return customerClient.getCustomer(t.getCustomerId())
                                                           .filter( (x -> x.getTypeCustomer() == 2) )
                                                           .hasElement()
                                                           .flatMap( yy -> {
                                                        	   return customerClient.getCustomer(t.getCustomerId()).flatMap(customerToSend -> {
                                                        		   return product.getProduct(t.getProductId()).flatMap(pToSend -> {
                                                        			   if ( zz  && yy && customerToSend.getTypeCustomer() == 2){
                                                                           return Mono.error(new RuntimeException("The business client cannot have a savings or fixed-term account"));
                                                                       } else if (customerToSend.getTypeCustomer() == 1 || customerToSend.getTypeCustomer() == 2) {
                                                                    	   if (t.getAvailableBalance().compareTo(BigDecimal.ZERO)<=0) {
                       	    		                              		  		t.setAvailableBalance(BigDecimal.ZERO);
                       	    		                              	  		}
                                                                    	   return maturedDebt(t);
                                                                       } else if (customerToSend.getTypeCustomer() == 3) {
                                                                    	   Boolean b = pToSend.getAmountPerDay().compareTo(BigDecimal.ZERO)>0 && pToSend.getAmountPerMonth().compareTo(BigDecimal.ZERO)>0;
                                                                    	   return personalVipEmpresaPymeValidation(t, customerToSend.getTypeCustomer(), 1, b); // Cuenta de ahorro para Personal Vip
                                                                       } else {
                                                                    	   
                                                                    	   return personalVipEmpresaPymeValidation(t, customerToSend.getTypeCustomer(), 2 , true); // Cuenta corriente Empresarial Pyme
                                                                       }
                                                        		   });
                                                        		   
                                                        	   });
                                                               
                                                           });
                                               });
                                   }
                                });
                    }
                });
    }
    
    /*
     * Indica si existe una deuda pendiente 
     */
    private Mono<Transaction> maturedDebt (Transaction transaction) {
    	return findAllWithDetail().filter(trans -> trans.getCustomerId().equalsIgnoreCase(transaction.getCustomerId()))
    		.filter(trans -> trans.getProduct().getIndProduct() == 1)
    		.filter(trans -> trans.getMaturedDebt() == 1)
    		.hasElements() // 1 indica que hay un producto de credito con deuda vencida
    		.flatMap(deuda -> {
    			if(deuda) {
    				return Mono.error(new RuntimeException("Tiene una deuda vencida"));
    			} else {
    				return transactionRepository.save(transaction);
    			}
    			
    		}); 
    }
    
    public Mono<Transaction> personalVipEmpresaPymeValidation(Transaction t, Integer typeCostumerToValidate, Integer typeProductToValidate, Boolean f) {
    	return hasCreditCard(t).filter(btc -> btc.equals(Boolean.TRUE))
    			.hasElements()
    			.flatMap(bTarjetaCredito -> {
    					if (Boolean.TRUE.equals(bTarjetaCredito)) {
    						return product.getProduct(t.getProductId())
    	    		                .filter( x -> (x.getTypeProduct() == typeProductToValidate && f)
    	    		                		|| x.getTypeProduct() == 6)
    	    		                .hasElement()
    	    		                .flatMap( bp -> {
    	    		                    return customerClient.getCustomer(t.getCustomerId())
    	    		                            .filter( (x -> x.getTypeCustomer() == typeCostumerToValidate) )
    	    		                            .hasElement()
    	    		                            .flatMap( bc -> {
    	    		                                if ( bp  && bc ){
    	    		                                	if (t.getAvailableBalance().compareTo(BigDecimal.ZERO)<=0) {
    	    		                              		  	t.setAvailableBalance(BigDecimal.ZERO);
    	    		                              	  	}
    	    		                                	// Comision 0 para personal vip y empresarial pyme
    	    		                                	t.setMaintenanceCommission(BigDecimal.ZERO);
    	    		                                    return maturedDebt(t);
    	    		                                    //
    	    		                                }else{
    	    		                                	return Mono.error(new RuntimeException("No se pudo crear una cuenta de ahorro para Personal Vip, no cumple las condiciones"));
    	    		                                }
    	    		                            });
    	    		                });
    					} else {
    						return product.getProduct(t.getProductId())
    	    		                .filter( x -> ( x.getTypeProduct() == 6 ))
    	    		                .hasElement()
    	    		                .flatMap( bp -> {
    	    		                	if (bp) {
    	    		                		if (t.getAvailableBalance().compareTo(BigDecimal.ZERO)<=0) {
		                              		  	t.setAvailableBalance(BigDecimal.ZERO);
		                              	  	}
    	    		                		// Comision 0 para personal vip y empresarial pyme
    	    		                		t.setMaintenanceCommission(BigDecimal.ZERO);
    	    		                		return maturedDebt(t);
										} else {
											return Mono.error(new RuntimeException("Solo puede crearse cuenta de ahorro para Personal Vip, si tiene tarjeta de credito"));
										}
    	    		                });
    					}
    			});
    }
    
    public Flux<Boolean> hasCreditCard(Transaction t) {
    	//transaction - productId - evaluarProductId - boolean
    	return findAll().filter(trans -> trans.getCustomerId().equalsIgnoreCase(t.getCustomerId()))
    			.flatMap(trans -> {
    				return product.getProduct(trans.getProductId()).filter(prod -> prod.getTypeProduct() == 6)
    						.hasElement();			
    			});
    }

    @Override
    public Mono<Transaction> findById(String id) {
        log.info("Method call FindById - transaction");
        return transactionRepository.findById(id);
    }

    @Override
    public Mono<Transaction> update(Transaction t, String id) {
        log.info("Method call Update - transaction");
        return transactionRepository.findById(id)
                .map( x -> {
                    x.setProductId(t.getProductId());
                    x.setAccountNumber(t.getAccountNumber());
                    x.setMovementLimit(t.getMovementLimit());
                    x.setCreditLimit(t.getCreditLimit());
                    x.setAvailableBalance(t.getAvailableBalance());
                    x.setMaintenanceCommission(t.getMaintenanceCommission());
                    x.setCardNumber(t.getCardNumber());
                    x.setRetirementDateFixedTerm(t.getRetirementDateFixedTerm());
                    x.setMaxAmountTransaction(t.getMaxAmountTransaction());
                    x.setCurrentNumberTransaction(t.getCurrentNumberTransaction());
                    x.setRegistrationDate(t.getRegistrationDate());
                    x.setCreditCardAssociationDate(t.getCreditCardAssociationDate());
                    return x;
                }).flatMap(transactionRepository::save);
    }

    @Override
    public Mono<Transaction> delete(String id) {
        log.info("Method call Delete - transaction");
        return transactionRepository.findById(id).flatMap( x -> transactionRepository.delete(x).then(Mono.just(Transaction.builder().id(id).build())));
    }

    @Override
    public Mono<Transaction> findByIdWithCustomer(String id) {
        log.info("Method call FindByIdWithCustomer - transaction");
        return transactionRepository.findById(id)
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMap( customer -> {
                            return product.getProduct(trans.getProductId())
                                    .flatMap( product -> {
                                        return depositClient.getDeposit()
                                                .filter(x -> x.getTransactionId().equals(trans.getId()))
                                                .collectList()
                                                .flatMap((deposit -> {
                                                    return withDrawalClient.getWithDrawal()
                                                           .filter(i -> i.getTransactionId().equals(trans.getId()))
                                                           .collectList()
                                                           .flatMap(( withdrawals -> {
                                                               return paymentClient.getPayment()
                                                                       .filter(z -> z.getTransactionId().equals(trans.getId()))
                                                                       .collectList()
                                                                       .flatMap((payments -> {
                                                               return purchaseClient.getPurchase()
                                                                       .filter(y -> y.getTransactionId().equals(trans.getId()))
                                                                       .collectList()
                                                                       .flatMap(purchases -> {

                                                                           return signatoryClient.getSignatory()
                                                                                   .filter(o -> o.getTransactionId().equals(trans.getId()))
                                                                                   .collectList()
                                                                                   .flatMap(signatories -> {
                                                                                       ValorAllValidator(trans, customer, product, deposit, withdrawals, payments, purchases, signatories);
                                                                                       return Mono.just(trans);
                                                                                   });
                                                                       });

                                                                       } ));
                                                   } ));
                                    }));
                        });
            }));
    }

	@Override
	public Flux<Transaction> findAllWithDetail() {
        return transactionRepository.findAll()
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMapMany( customer -> {
                            return product.getProduct(trans.getProductId())
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
    
    @Override
	public Flux<Transaction> lastTenMovements() {
    	return transactionRepository.findAll()
                .flatMap( trans -> customerClient.getCustomer(trans.getCustomerId())
                        .flatMapMany( customer -> {
                            return product.getProduct(trans.getProductId())
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
                                                                                                //ValorAllValidator(trans, customer, product, deposit, withdrawals, payments, purchases, signatories);
                                                                                                List<DateInterface> x= new ArrayList<>();
                                                                                                x.addAll(deposit);
                                                                                                x.addAll(withdrawals);
                                                                                                x.addAll(payments);
                                                                                                x.addAll(purchases);
                                                                                                x.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                                                                                                log.info(x.toString());
                                                                                                return Flux.fromIterable(x).take(10).collectList().flatMapMany(to -> {
                                                                                                	trans.setMovements(to);
                                                                                                	return Flux.just(trans);
                                                                                                });
                                                                                                //trans.setMovements(x);
                                                                                                // (o1, o2) -> o1.getCreditCardAssociationDate().compareTo(o2.getCreditCardAssociationDate())
                                                                                                //return Flux.fromIterable(x);
                                                                                            });
                                                                                });

                                                                    } ));
                                                        } ));
                                            })));
                        }));
    }
}