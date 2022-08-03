package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "schema_ex.exchange")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {

    @Id
    private String id;
    private String customerId;
    private BigDecimal montoSoles;
    private BigDecimal montoBootCoin;
    private BigDecimal tazaCambio;
}
