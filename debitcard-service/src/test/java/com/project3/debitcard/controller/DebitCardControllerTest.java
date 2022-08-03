package com.project3.debitcard.controller;

import com.project3.debitcard.entity.Customer;
import com.project3.debitcard.entity.DebitCard;
import com.project3.debitcard.entity.Product;
import com.project3.debitcard.service.DebitCardService;
import com.project3.debitcard.service.impl.DebitCardServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
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

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = DebitCardController.class)
@Import(DebitCardServiceImpl.class)
public class DebitCardControllerTest {

    @MockBean
    private DebitCardService debitCardService;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void findAll() {
        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardService.findAll()).thenReturn(Flux.just(debitCard));

        webTestClient.get()
                .uri("/debitcard/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(DebitCard.class);

        Mockito.verify(debitCardService,times(1)).findAll();
    }

    @Test
    void FindById() {

        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardService.findById("578462f5dg452gdf")).thenReturn(Mono.just(debitCard));

        webTestClient.get()
                .uri("/debitcard/find/{id}","578462f5dg452gdf")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cardNumber").isNotEmpty()
                .jsonPath("$.id").isEqualTo("578462f5dg452gdf")
                .jsonPath("$.cardNumber").isEqualTo("246845445365326221")
                .jsonPath("$.customer").isEqualTo(new Customer())
                .jsonPath("$.product").isEqualTo(new Product());

        Mockito.verify(debitCardService,times(1)).findById("578462f5dg452gdf");

    }

    @Test
    void Delete() {

        DebitCard debitCard = DebitCard.builder()
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        String id = "unhb2342342342";
        Mockito.when(debitCardService.delete(id))
                .thenReturn(Mono.just(debitCard));

        webTestClient.delete().uri("/debitcard/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(debitCardService,times(1)).delete(id);

    }

    @Test
    void updateCustomerTest() {
        DebitCard debitCard = DebitCard.builder()
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        String id = "unhb2342342342";

        Mockito.when(debitCardService.update(debitCard, id)).thenReturn(Mono.just(debitCard));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/debitcard/update/{id}")
                        .build(id))
                .body(Mono.just(debitCard), Customer.class)
                .exchange()
                .expectStatus().isOk();
        Mockito.verify(debitCardService,times(1)).update(debitCard,id);
    }

    @Test
    void createCustomerTest() {
        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardService.create(Mockito.any())).thenReturn(Mono.just(debitCard));

        webTestClient.post().uri("/debitcard/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(debitCard))
                .exchange()
                .expectStatus()
                .isOk().expectBody();


    }

    @Test
    void accountDetail() {

        Customer customerMono = Customer.builder()
                .id(ObjectId.get().toString())
                .name("yasmin")
                .lastName("zegarra")
                .docNumber("2342342342")
                .typeCustomer(1)
                .descTypeCustomer("personal").build();

        Product productMono = Product.builder()
                .id(ObjectId.get().toString())
                .indProduct(1)
                .descIndProduct("cuenta bancaria")
                .typeProduct(1)
                .descTypeProduct("cuenta de ahorro")
                .build();

        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(customerMono)
                .product(productMono).build();

        Mockito.when(debitCardService.accountDetail("246845445365326221")).thenReturn(Flux.just(debitCard));

        webTestClient.get()
                .uri("/debitcard/accountDetail/{id}","246845445365326221")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.cardNumber").isNotEmpty()
                .jsonPath("$.id").isEqualTo("578462f5dg452gdf")
                .jsonPath("$.cardNumber").isEqualTo("246845445365326221")
                .jsonPath("$.customer").isEqualTo(customerMono)
                .jsonPath("$.product").isEqualTo(productMono);


        Mockito.verify(debitCardService,times(1)).accountDetail("246845445365326221");

    }
}
