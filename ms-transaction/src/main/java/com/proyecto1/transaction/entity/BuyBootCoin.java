package com.proyecto1.transaction.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyBootCoin {

    private String id;
    private String walletId;
    private String customerIdEmisor;
    private String accountIdReceptor;
    private BigDecimal monto;
    private String state;
    private String modoDePago;
    private Integer buyBootCoinId;


}
