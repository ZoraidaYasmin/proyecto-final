package com.proyecto1.transaction.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DebitCard {
	
	@Id
	private String id;
	private String cardNumber;
	
	@Transient
    private Customer customer;
    
    @Transient
    private Product product;
    
    @Transient
    private List<Transaction> transaction;
    
    @Transient
    private Transaction trans;

    @Transient
    private List<Deposit> deposit;

    @Transient
    private List<Withdrawal> withdrawal;

    @Transient
    private List<Payment> payments;

    @Transient
    private List<Purchase> purchases;

    @Transient
    private List<Signatory> signatories;
}
