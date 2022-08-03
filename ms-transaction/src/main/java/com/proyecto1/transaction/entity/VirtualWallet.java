package com.proyecto1.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirtualWallet {
	

	private String id;
	private String dni;
	private String cellphone;
	private String operation;
	private BigDecimal amount;
	private String cardNumberEmisor;
	private String cardNumberReceptor;

}
