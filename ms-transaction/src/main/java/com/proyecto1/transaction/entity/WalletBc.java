package com.proyecto1.transaction.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletBc {
	
	private String id;
	
	private String numeroDocumento;
	
	private String celular;
	
	private String correoElectronico;
	
	private BigDecimal montoDeBcTotal;
	
	private String customerId;
	
}
