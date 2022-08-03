package com.proyecto1.transaction.client;

import com.proyecto1.transaction.entity.BuyBootCoin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeEvent {

    private Integer exchangeId;
    private String id;
    private String customerId;
    private BigDecimal montoSoles;
    private BigDecimal montoBootCoin;
    private BigDecimal tazaCambio;
    private String buyBootCoinId;

    private BuyBootCoin buyBootCoin;
}
