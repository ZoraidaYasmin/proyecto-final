package com.proyecto1.product.controller;

import com.proyecto1.product.entity.Customer;
import com.proyecto1.product.entity.Product;
import com.proyecto1.product.entity.Transaction;
import com.proyecto1.product.service.ProductService;
import com.proyecto1.product.service.impl.ProductServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ProductController.class)
@Import(ProductServiceImpl.class)
public class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    ProductService productService;

    @Test
     void createDepositTest() {
        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        Mockito.when(productService.create(Mockito.any())).thenReturn(Mono.just(productMono));

        webTestClient.post().uri("/product/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(productMono))
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productService,times(1)).create(productMono);
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
        Mockito.when(productService.update(productMono, id))
                .thenReturn(Mono.just(productMono));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/product/update/{id}")
                        .build(id))
                .body(Mono.just(productMono), Product.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(productService,times(1)).update(productMono,id);
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

        Mockito.when(productService.findAll()).thenReturn(Flux.just(productMono));

        webTestClient.get()
                .uri("/product/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Product.class);

        Mockito.verify(productService,times(1)).findAll();
    }

    @Test
     void FindById() {

        Product product = new Product();
        product.setId("12buhvg24uhjknv2");
        product.setIndProduct(1);
        product.setDescIndProduct("cuenta bancaria");
        product.setTypeProduct(1);
        product.setDescTypeProduct("cuenta de ahorro");

        Mockito.when(productService.findById("12buhvg24uhjknv2")).thenReturn(Mono.just(product));

        webTestClient.get()
                .uri("/product/find/{id}","12buhvg24uhjknv2")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.indProduct").isNotEmpty()
                .jsonPath("$.id").isEqualTo("12buhvg24uhjknv2")
                .jsonPath("$.indProduct").isEqualTo(1)
                .jsonPath("$.descIndProduct").isEqualTo("cuenta bancaria")
                .jsonPath("$.typeProduct").isEqualTo(1)
                .jsonPath("$.descTypeProduct").isEqualTo("cuenta de ahorro");


        Mockito.verify(productService,times(1)).findById("12buhvg24uhjknv2");
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
        String id = "6767668789fds9";

        Mockito.when(productService.delete(id))
                .thenReturn(Mono.just(productMono));

        webTestClient.delete().uri("/product/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(productService,times(1)).delete(id);

    }
    @Test
    void reportByDate(){
        Transaction transactionMono = Transaction.builder()
                .id(ObjectId.get().toString())
                .customerId("23424242345fdd")
                .productId("242342j3nji234")
                .depositId("234234u89534hufinf")
                .accountNumber("38748398273492734")
                .movementLimit(1)
                .creditLimit(BigDecimal.valueOf(300))
                .availableBalance(BigDecimal.valueOf(200))
                .maintenanceCommission(BigDecimal.valueOf(2.00))
                .cardNumber("2732648729238423742")
                .registrationDate(LocalDate.now())
                .retirementDateFixedTerm(LocalDate.now())

                .customer(new Customer())
                .product(new Product())
                .build();

        Mockito.when(productService.reportByDate(transactionMono.getRegistrationDate(), transactionMono.getRegistrationDate())).thenReturn(Flux.just(transactionMono));

        webTestClient.get()
                .uri("/product/reportByDate/2022-07-26/2022-07-26")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Product.class);

        Mockito.verify(productService,times(1)).reportByDate(transactionMono.getRegistrationDate(), transactionMono.getRegistrationDate());
    }
}
