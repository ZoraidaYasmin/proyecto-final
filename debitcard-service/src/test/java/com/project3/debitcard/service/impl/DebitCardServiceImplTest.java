package com.project3.debitcard.service.impl;

import com.project3.debitcard.entity.Customer;
import com.project3.debitcard.entity.DebitCard;
import com.project3.debitcard.entity.Product;
import com.project3.debitcard.repository.DebitCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class DebitCardServiceImplTest {

    @Mock
    private DebitCardRepository debitCardRepository;

    @InjectMocks
    private DebitCardServiceImpl debitCardServiceImpl;

    @Test
    void create() {
        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardRepository.save(Mockito.any())).thenReturn(Mono.just(debitCard));

        assertDoesNotThrow(() -> debitCardServiceImpl.create(debitCard)
                .subscribe(response -> {
                    assertEquals(debitCard.getCardNumber(), response.getCardNumber());
                }));
    }

    @Test
    void updateTest() {
        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        String id = "unhb2342342342";

        Mockito.when(debitCardRepository.findById(id)).thenReturn(Mono.just(debitCard));
        Mockito.when(debitCardRepository.save(debitCard)).thenReturn(Mono.just(debitCard));

        assertDoesNotThrow(() -> debitCardServiceImpl.update(debitCard, id)
                .subscribe(response -> {
                    assertEquals(debitCard.getCardNumber(), response.getCardNumber());
                }));
    }

    @Test
    void findAll() {
        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardRepository.findAll()).thenReturn(Flux.just(debitCard));
        assertDoesNotThrow(() -> debitCardServiceImpl.findAll()
                .subscribe(response -> {
                    assertEquals(debitCard.getCardNumber(), response.getCardNumber());
                }));
    }

    @Test
    void FindById() {

        DebitCard debitCard = DebitCard.builder()
                .id("578462f5dg452gdf")
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        Mockito.when(debitCardRepository.findById("578462f5dg452gdf")).thenReturn(Mono.just(debitCard));
        assertDoesNotThrow(() -> debitCardServiceImpl.findById(debitCard.getId())
                .subscribe(response -> {
                    assertEquals(debitCard.getCardNumber(), response.getCardNumber());
                }));
    }

    @Test
    void Delete() {
        DebitCard debitCard = DebitCard.builder()
                .cardNumber("246845445365326221")
                .customer(new Customer())
                .product(new Product()).build();

        String id = "unhb2342342342";

        Mockito.when(debitCardRepository.findById("unhb2342342342")).thenReturn(Mono.just(debitCard));
        Mockito.when(debitCardRepository.delete(debitCard)).thenReturn(Mono.empty());

        assertDoesNotThrow(() -> debitCardServiceImpl.delete(id)
                .subscribe(response -> {
                    assertEquals(new DebitCard(), response);
                }));
    }


}
