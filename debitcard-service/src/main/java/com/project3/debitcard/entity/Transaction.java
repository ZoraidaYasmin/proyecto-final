package com.project3.debitcard.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class Transaction {
    
    private String id;

    private String customerId;
    private String productId;
    private String depositId;
    private String accountNumber;
    private int movementLimit;
    private BigDecimal creditLimit;
    private BigDecimal availableBalance;
    private BigDecimal maintenanceCommission;
    private String cardNumber;
    private Integer maxAmountTransaction;
    private Integer currentNumberTransaction; 
    private int maturedDebt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate creditCardAssociationDate;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate registrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate retirementDateFixedTerm;

    @Transient
    private Customer customer;
    
    @Transient
    private Product product;

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
