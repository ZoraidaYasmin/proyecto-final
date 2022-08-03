package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
@Document(collection = "schema_buy.buyBootCoin")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyBootCoin {

    @Id
    private String id;
    private String walletId;
    private String customerIdEmisor;
    private String accountIdReceptor;
    private BigDecimal monto;
    private String state;
    private String modoDePago;
    private Integer buyBootCoinId;
    
    @Transient
    private WalletBc walletBc;

}
