package com.proyecto1.product.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private String id;
    private String name;
    private String lastName;
    private String docNumber;
    private int typeCustomer;
    private String descTypeCustomer;
}
