package com.proyecto1.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String id;
    private String name;
    private String lastName;
    private String docNumber;
    private int typeCustomer;
    private String descTypeCustomer;
    private ReporteDTO reporteDTO;
}
