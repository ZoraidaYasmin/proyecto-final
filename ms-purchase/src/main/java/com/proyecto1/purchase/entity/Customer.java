package com.proyecto1.purchase.entity;

import lombok.Data;


@Data
public class Customer {


    private String id;
    private String name;
    private String lastName;
    private String docNumber;
    private int typeCustomer;
    private String descTypeCustomer;
}
