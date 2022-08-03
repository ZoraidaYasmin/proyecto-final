package com.proyecto1.transaction.service.impl;

import com.proyecto1.transaction.client.*;
import com.proyecto1.transaction.entity.*;
import com.proyecto1.transaction.repository.TransactionRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private DepositClient depositClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private PurchaseClient purchaseClient;

    @Mock
    private SignatoryClient signatoryClient;

    @Mock
    private WithDrawalClient withDrawalClient;

    @InjectMocks
    private TransacionServiceImpl transacionServiceImpl;

    private static Customer customer;
    private static Product product;
    private static Deposit deposit;
    private static Withdrawal withdrawal;

    private static Signatory signatory;

    private static Payment payment;

    private static Purchase purchase;

    private static Transaction transactionMono;
    @BeforeAll
    static void setup(){
         customer = Customer.builder()
                .id("963258741")
                .name("yasmin2")
                .lastName("oyarce")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

         product = Product.builder()
                .id("242342j3nji234")
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

         deposit = Deposit.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .description("demo 1")
                .transactionId("84374234y743123")
                .build();

         withdrawal = Withdrawal.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .withdrawalAmount(BigDecimal.valueOf(2000))
                .description("demo withdrawal")
                .transactionId("84374234y743123")
                .build();

         signatory = Signatory.builder()
                .id(ObjectId.get().toString())
                .name("yasmin")
                .lastName("zegarra")
                .docNumber("8797897797")
                .transactionId("84374234y743123")
                .build();

         payment = Payment.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("demo payment")
                .transactionId("234234jnjk2345")
                .build();

         purchase = Purchase.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("demo purchase")
                .transactionId("34242423234")
                .build();

         transactionMono = Transaction.builder()
                .id("6767668789fds9")
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .depositId("234234u89534hufinf")
                .accountNumber("38748398273492734")
                .movementLimit(1)
                .creditLimit(BigDecimal.valueOf(300))
                .availableBalance(BigDecimal.valueOf(200))
                .maintenanceCommission(BigDecimal.valueOf(2.00))
                .cardNumber("2732648729238423742")
                .retirementDateFixedTerm(LocalDate.now())
                .customer(customer)
                .product(product)
                .build();
    }
    @Test
     void findAllWithDetail(){

        Mockito.when(transactionRepository.findAll()).thenReturn(Flux.just(transactionMono));
        Mockito.when(customerClient.getCustomer(transactionMono.getCustomerId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(transactionMono.getProductId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposit()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithDrawal()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayment()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchase()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatory()).thenReturn(Flux.just(signatory));


      /*  assertDoesNotThrow(() -> transacionServiceImpl.findAllWithDetail()
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));*/

        Flux<Transaction> result = transacionServiceImpl.findAllWithDetail();

        StepVerifier.create(result)
                .assertNext(r -> assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber()))
                .verifyComplete();
    }

    @Test
     void updateDepositTest() {

        String id = "6767668789fds9";

        Mockito.when(transactionRepository.findById(id)).thenReturn(Mono.just(transactionMono));
        Mockito.when(transactionRepository.save(transactionMono)).thenReturn(Mono.just(transactionMono));

      /*  assertDoesNotThrow(() -> transacionServiceImpl.update(transactionMono,id)
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));*/

        Mono<Transaction> resultfind = transacionServiceImpl.findById(
                id
        );

        StepVerifier.create(resultfind)
                .assertNext(r -> assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber()))
                .verifyComplete();

        Mono<Transaction> result = transacionServiceImpl.update(
                transactionMono,
                id
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();

    }

    @Test
     void findAll() {


        Mockito.when(transactionRepository.findAll()).thenReturn(Flux.just(transactionMono));

      /*  assertDoesNotThrow(() -> transacionServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));*/

        Flux<Transaction> result = transacionServiceImpl.findAll();

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();
    }

    @Test
     void FindById() {

        Mockito.when(transactionRepository.findById("6767668789fds9")).thenReturn(Mono.just(transactionMono));

      /*  assertDoesNotThrow(() -> transacionServiceImpl.findById(transactionMono.getId())
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));*/

        Mono<Transaction> result = transacionServiceImpl.findById(
                transactionMono.getId()
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();
    }

    @Test
     void Delete() {

        String id = "6767668789fds9";

        Mockito.when(transactionRepository.findById("6767668789fds9")).thenReturn(Mono.just(transactionMono));
        Mockito.when(transactionRepository.delete(transactionMono)).thenReturn(Mono.empty());

        /*
        assertDoesNotThrow(() -> transacionServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Transaction(), response);
                }));*/

        Mono<Transaction> resultfind = transacionServiceImpl.findById(
                id
        );

        StepVerifier.create(resultfind)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();

        Mono<Transaction> result = transacionServiceImpl.delete(
                id
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getId(), r.getId());
                })
                .verifyComplete();

    }

    @Test
     void findByIdWithCustomer(){

        Mockito.when(transactionRepository.findById(transactionMono.getId())).thenReturn(Mono.just(transactionMono));
        Mockito.when(customerClient.getCustomer(transactionMono.getCustomerId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(transactionMono.getProductId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposit()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithDrawal()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayment()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchase()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatory()).thenReturn(Flux.just(signatory));

        Mono<Transaction> result = transacionServiceImpl.findByIdWithCustomer(
                transactionMono.getId()
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();

    }

    @Test
    void saveTransaction(){

        Mockito.when(transactionRepository.findAll()).thenReturn(Flux.just(transactionMono));
        Mockito.when(customerClient.getCustomer(transactionMono.getCustomerId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(transactionMono.getProductId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposit()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithDrawal()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayment()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchase()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatory()).thenReturn(Flux.just(signatory));

        Mono<Transaction> result = transacionServiceImpl.save(
                transactionMono
        );

        StepVerifier.create(result).expectError(RuntimeException.class);

    }


}
