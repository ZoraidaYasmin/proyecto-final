package com.proyecto1.withdrawal.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate retirementDateFixedTerm;


    private Customer customer;


    private Product product;


    private List<Deposit> deposit;


    private List<Withdrawal> withdrawal;

    private List<Payment> payments;
}
