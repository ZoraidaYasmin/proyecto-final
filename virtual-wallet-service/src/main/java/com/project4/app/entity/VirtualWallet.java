package com.project4.app.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "schema_dep.virtualWallets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VirtualWallet {
	
	@Id
	private String id;
	private String dni;
	private String cellphone;
	private String operation;
	private BigDecimal amount;
	private String cardNumberEmisor;
	private String cardNumberReceptor;
	// customerid
}
