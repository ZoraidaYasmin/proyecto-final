package com.proyecto1.customer.service.impl;

import com.proyecto1.customer.client.*;
import com.proyecto1.customer.controller.CustomerController;
import com.proyecto1.customer.dto.CustomerDTO;
import com.proyecto1.customer.entity.*;
import com.proyecto1.customer.repository.CustomerRepository;
import com.proyecto1.customer.service.CustomerService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionClient transactionClient;

    @Mock
    private ProductClient productClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private PurchaseClient purchaseClient;

    @Mock
    private SignatoryClient signatoryClient;

    @Mock
    private WithDrawalClient withDrawalClient;

    @Mock
    private DepositClient depositClient;

    static Customer customerOk = new Customer();

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;
    @Test
    void summaryCustomerByProduct(){
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
        Mockito.when(customerRepository.findById(transactionMono.getCustomerId())).thenReturn(Mono.just(customer));
        Mockito.when(productClient.getProduct(transactionMono.getProductId())).thenReturn(Mono.just(product));
        Mockito.when(depositClient.getDeposit()).thenReturn(Flux.just(deposit));
        Mockito.when(withDrawalClient.getWithDrawal()).thenReturn(Flux.just(withdrawal));
        Mockito.when(paymentClient.getPayment()).thenReturn(Flux.just(payment));
        Mockito.when(purchaseClient.getPurchase()).thenReturn(Flux.just(purchase));
        Mockito.when(signatoryClient.getSignatory()).thenReturn(Flux.just(signatory));


        /*assertDoesNotThrow(() -> customerServiceImpl.summaryCustomerByProduct()
                .subscribe(response -> {
                    assertEquals(transactionMono.getAccountNumber(), response.getAccountNumber());
                }));*/


        Flux<Transaction> result = customerServiceImpl.summaryCustomerByProduct();

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(transactionMono.getAccountNumber(), r.getAccountNumber());
                })
                .verifyComplete();
    }
    @Test
    void createCustomerTest() {
        CustomerDTO customerMono = CustomerDTO.builder()
                .id(ObjectId.get().toString())
                .name("yasmin")
                .lastName("zegarra")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerMono,customer);

        Mockito.when(customerRepository.save(Mockito.any())).thenReturn(Mono.just(customer));

        assertDoesNotThrow(() -> customerServiceImpl.create(customerMono)
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));

    }

    @Test
    void updateCustomerTest() {
        CustomerDTO customerMono = CustomerDTO.builder()
                .name("yasmin")
                .lastName("oyarce")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();
        String id = "unhb2342342342";

        Customer customer = new Customer();
        BeanUtils.copyProperties(customerMono,customer);

        Mockito.when(customerRepository.findById(id)).thenReturn(Mono.just(customer));

        Mockito.when(customerRepository.save(customer)).thenReturn(Mono.just(customer));

        assertDoesNotThrow(() -> customerServiceImpl.update(customerMono, id)
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));

    }

    @Test
    void findAll() {
        Customer customerMono = Customer.builder()
                .name("yasmin")
                .lastName("oyarce")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Mockito.when(customerRepository.findAll()).thenReturn(Flux.just(customerMono));

        assertDoesNotThrow(() -> customerServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(customerMono.getLastName(), response.getLastName());
                }));
    }

    @Test
    void FindById() {

        Customer customer = new Customer();
        customer.setId("12buhvg24uhjknv2");
        customer.setName("zoraida");
        customer.setLastName("oyarce");
        customer.setDocNumber("23424234234");
        customer.setTypeCustomer(1);
        customer.setDescTypeCustomer("personal");

        Mockito.when(customerRepository.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(customer));

        assertDoesNotThrow(() -> customerServiceImpl.findById(customer.getId())
                .subscribe(response -> {
                    assertEquals(customer.getLastName(), response.getLastName());
                }));

    }

    @Test
    void Delete() {

        Customer customerMono = Customer.builder()
                .name("yasmin")
                .lastName("oyarce")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        String id = "unhb2342342342";

        Mockito.when(customerRepository.findById("unhb2342342342")).thenReturn(Mono.just(customerMono));
        Mockito.when(customerRepository.delete(customerMono)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> customerServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new Customer(), response);
                }));

    }
}
