package com.project3.debitcard.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	private String id;
    private int indProduct;
    private String descIndProduct;
    private int typeProduct;
    private String descTypeProduct;
    private BigDecimal amountPerMonth;
    private BigDecimal amountPerDay;
}
