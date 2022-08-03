package com.proyecto1.deposit.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
@Builder
public class Signatory {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String docNumber;
    private String transactionId;
}
