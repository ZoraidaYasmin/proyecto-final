package com.proyecto1.customer.dto;

import java.math.BigDecimal;

import com.proyecto1.customer.entity.Product;

import lombok.Data;

@Data
public class ReporteDTO {
	
	private BigDecimal averageDailyBalancesInOneMonth;
	private Product product;
}
