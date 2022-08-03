package com.example.demo.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Document(collection = "schema_account.walletBc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletBc {
	
	@Id
	private String id;
	
	private String numeroDocumento;
	
	private String celular;
	
	private String correoElectronico;
	
	private BigDecimal montoDeBcTotal;
	
	private String customerId;
	
}
