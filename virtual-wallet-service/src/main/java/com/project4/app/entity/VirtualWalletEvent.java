package com.project4.app.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VirtualWalletEvent {
	private Integer virtualWalletId;
	private String id;
	private String dni;
	private String cellphone;
	private String operation;
	private BigDecimal amount;
	private String cardNumberEmisor;
	private String cardNumberReceptor;
}
