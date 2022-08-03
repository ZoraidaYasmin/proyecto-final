package com.project4.app.controller;

import com.project4.app.entity.VirtualWallet;
import com.project4.app.producer.VirtualWalletProducer;
import com.project4.app.service.VirtualWalletService;
import com.project4.app.service.impl.VirtualWalletServiceImpl;
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
@WebFluxTest(controllers = VirtualWalletController.class)
@Import({VirtualWalletServiceImpl.class, VirtualWalletProducer.class})
public class VirtualWalletControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private VirtualWalletService virtualWalletService;

    @MockBean
    private VirtualWalletProducer virtualWalletProducer;

    @Test
    void findAll() {

        VirtualWallet virtualWallet = VirtualWallet.builder()
                .id(ObjectId.get().toString())
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();

        Mockito.when(virtualWalletService.findAll()).thenReturn(Flux.just(virtualWallet));

        webTestClient.get()
                .uri("/virtualWallet/findAll")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(VirtualWallet.class);

         Mockito.verify(virtualWalletService, times(1)).findAll();
    }

    @Test
    void FindById() {

        VirtualWallet virtualWallet = VirtualWallet.builder()
                .id("6562626656565656")
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();

        Mockito.when(virtualWalletService.findById("6562626656565656")).thenReturn(Mono.just(virtualWallet));

        webTestClient.get()
                .uri("/virtualWallet/find/{id}", "6562626656565656")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.id").isEqualTo("6562626656565656")
                .jsonPath("$.dni").isEqualTo("3824334665445")
                .jsonPath("$.cellphone").isEqualTo("976348221")
                .jsonPath("$.operation").isEqualTo("E")
                .jsonPath("$.amount").isEqualTo(BigDecimal.valueOf(100))
                .jsonPath("$.cardNumberEmisor").isEqualTo("3636546454756756")
                .jsonPath("$.cardNumberReceptor").isEqualTo("0")
                .jsonPath("$.virtualWalletId").isEqualTo(1);

         Mockito.verify(virtualWalletService, times(1)).findById("6562626656565656");
    }

    @Test
    void Delete() {

        VirtualWallet virtualWallet = VirtualWallet.builder()
                .id("6562626656565656")
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();

        String id = "6767668789fds9";

        Mockito.when(virtualWalletService.findById(id)).thenReturn(Mono.just(virtualWallet));
        Mockito.when(virtualWalletService.delete(id)).thenReturn(Mono.just(virtualWallet));

        webTestClient.delete().uri("/virtualWallet/delete/{id}", id)
                .exchange()
                .expectStatus().isOk();

         Mockito.verify(virtualWalletService,times(1)).delete(id);

    }

    @Test
    void updateTransactionTest() {
        VirtualWallet virtualWallet = VirtualWallet.builder()
                .id("6562626656565656")
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();
        String id = "6767668789fds9";

        Mockito.when(virtualWalletService.findById(id)).thenReturn(Mono.just(virtualWallet));
        Mockito.when(virtualWalletService.update(virtualWallet, id)).thenReturn(Mono.just(virtualWallet));

        webTestClient.put().uri(uriBuilder -> uriBuilder
                        .path("/virtualWallet/update/{id}")
                        .build(id))
                .body(Mono.just(virtualWallet), VirtualWallet.class)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(virtualWalletService,times(1)).update(virtualWallet,id);
    }

    @Test
    void createTest() {
        VirtualWallet virtualWallet = VirtualWallet.builder()
                .id("6562626656565656")
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();

        Mockito.when(virtualWalletService.create(virtualWallet)).thenReturn(Mono.just(virtualWallet));
        webTestClient.post().uri("/virtualWallet/create")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(virtualWallet))
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(virtualWalletService,times(1)).create(virtualWallet);
    }
}
