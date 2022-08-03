package com.project4.app.service.impl;

import com.project4.app.entity.VirtualWallet;
import com.project4.app.repository.VirtualWalletRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class VirtualWalletServiceImplTest {
    @Mock
    private VirtualWalletRepository virtualWalletRepository;

    @InjectMocks
    private VirtualWalletServiceImpl virtualWalletServiceImpl;

    private static VirtualWallet virtualWallet;

    @BeforeAll
    static void setup(){

         virtualWallet = VirtualWallet.builder()
                .id("6767668789fds9")
                .dni("3824334665445")
                .cellphone("976348221")
                .operation("E")
                .amount(BigDecimal.valueOf(100))
                .cardNumberEmisor("3636546454756756")
                .cardNumberReceptor("0")
                //.virtualWalletId(1)
                .build();
    }

    @Test
    void findAll() {
        Mockito.when(virtualWalletRepository.findAll()).thenReturn(Flux.just(virtualWallet));

        Flux<VirtualWallet> result = virtualWalletServiceImpl.findAll();

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getDni(), r.getDni());
                })
                .verifyComplete();
    }


    @Test
    void FindById() {

        Mockito.when(virtualWalletRepository.findById("6767668789fds9")).thenReturn(Mono.just(virtualWallet));

        Mono<VirtualWallet> result = virtualWalletServiceImpl.findById(
                virtualWallet.getId()
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getDni(), r.getDni());
                })
                .verifyComplete();
    }

    @Test
    void Delete() {

        String id = "6767668789fds9";
        Mockito.when(virtualWalletRepository.findById("6767668789fds9")).thenReturn(Mono.just(virtualWallet));
        Mockito.when(virtualWalletRepository.delete(virtualWallet)).thenReturn(Mono.empty());

        Mono<VirtualWallet> resultfind = virtualWalletServiceImpl.findById(virtualWallet.getId());

        StepVerifier.create(resultfind)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getId(), r.getId());
                })
                .verifyComplete();

        Mono<VirtualWallet> result =  virtualWalletServiceImpl.delete(virtualWallet.getId());

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getId(), r.getId());
                })
                .verifyComplete();

    }

    @Test
    void updateTest() {

        String id = "6767668789fds9";
        Mockito.when(virtualWalletRepository.findById(id)).thenReturn(Mono.just(virtualWallet));
        Mockito.when(virtualWalletRepository.save(virtualWallet)).thenReturn(Mono.just(virtualWallet));
        Mono<VirtualWallet> resultfind = virtualWalletServiceImpl.findById(id);

        StepVerifier.create(resultfind)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getDni(), r.getDni());
                })
                .verifyComplete();

        Mono<VirtualWallet> result = virtualWalletServiceImpl.update(
                virtualWallet,
                id);

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getDni(), r.getDni());
                })
                .verifyComplete();
    }

    @Test
    void saveTransaction(){

        Mockito.when(virtualWalletRepository.save(virtualWallet)).thenReturn(Mono.just(virtualWallet));


        Mono<VirtualWallet> result = virtualWalletServiceImpl.create(
                virtualWallet
        );

        StepVerifier.create(result)
                .assertNext(r -> {
                    assertEquals(virtualWallet.getDni(), r.getDni());
                })
                .verifyComplete();

    }
}
