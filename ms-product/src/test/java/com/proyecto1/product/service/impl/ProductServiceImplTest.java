package com.proyecto1.product.service.impl;

import com.proyecto1.product.client.*;
import com.proyecto1.product.entity.*;
import com.proyecto1.product.repository.ProductRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    @Mock
    private TransactionClient transactionClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private CustomerClient customerClient;

    @Mock
    private PurchaseClient purchaseClient;

    @Mock
    private SignatoryClient signatoryClient;

    @Mock
    private WithDrawalClient withDrawalClient;

    @Mock
    private DepositClient depositClient;
    @Test
     void createDepositTest() {
        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productRepository.save(Mockito.any())).thenReturn(Mono.just(productMono));

        assertDoesNotThrow(() -> productServiceImpl.create(productMono)
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
    }

    @Test
     void updateDepositTest() {
        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();
        String id = "6767668789fds9";
        Mockito.when(productRepository.findById(id)).thenReturn(Mono.just(productMono));
        Mockito.when(productRepository.save(productMono)).thenReturn(Mono.just(productMono));


        assertDoesNotThrow(() -> productServiceImpl.update(productMono, id)
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
    }

    @Test
     void findAll() {
        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productRepository.findAll()).thenReturn(Flux.just(productMono));

        assertDoesNotThrow(() -> productServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(productMono.getIndProduct(), response.getIndProduct());
                }));
    }
    @Test
     void FindById() {

        Product product = new Product();
        product.setId("12buhvg24uhjknv2");
        product.setIndProduct(1);
        product.setDescIndProduct("cuenta bancaria");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Mockito.when(productRepository.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(product));

        assertDoesNotThrow(() -> productServiceImpl.findById(product.getId())
                .subscribe(response -> {
                    assertEquals(product.getIndProduct(), response.getIndProduct());
                }));
    }

    @Test
     void Delete() {

        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();
        String id = "unhb2342342342";

        Mockito.when(productRepository.findById("unhb2342342342")).thenReturn(Mono.just(productMono));
        Mockito.when(productRepository.delete(productMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> productServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Product(), response);
                }));

    }

    @Test
    void reporteByDate(){
        Customer customer = Customer.builder()
                .id("23424242345fdd")
                .name("yasmin")
                .lastName("oyarce")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product product = Product.builder()
                .id("83457346534534")
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Deposit deposit = Deposit.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .depositAmount(BigDecimal.valueOf(200))
                .description("demo 1")
                .transactionId("84374234y743123")
                .build();

        Withdrawal withdrawal = Withdrawal.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .withdrawalAmount(BigDecimal.valueOf(2000))
                .description("demo withdrawal")
                .transactionId("84374234y743123")
                .build();

        Signatory signatory = Signatory.builder()
                .id(ObjectId.get().toString())
                .name("yasmin")
                .lastName("zegarra")
                .docNumber("8797897797")
                .transactionId("84374234y743123")
                .build();

        Payment payment = Payment.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .paymentAmount(BigDecimal.valueOf(200))
                .description("demo payment")
                .transactionId("234234jnjk2345")
                .build();

        Purchase purchase = Purchase.builder()
                .id(ObjectId.get().toString())
                .date(LocalDate.now())
                .purchaseAmount(BigDecimal.valueOf(100))
                .description("demo purchase")
                .transactionId("34242423234")
                .build();

        Transaction transactionMono = Transaction.builder()
                .id("84374234y743123")
                .customerId("23424242345fdd")
                .productId("83457346534534")
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

        Mockito.when(transactionClient.findAll()).thenReturn(Flux.just(transactionMono));
        Mockito.when(customerClient.getCustomer(transactionMono.getCustomerId())).thenReturn(Mono.just(customer));
        Mockito.when(productRepository.findById(transactionMono.getProductId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposit()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithDrawal()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayment()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchase()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatory()).thenReturn(Flux.just(signatory));


        assertDoesNotThrow(() -> productServiceImpl.reportByDate(transactionMono.getRegistrationDate(), transactionMono.getRegistrationDate())
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));
    }
}
