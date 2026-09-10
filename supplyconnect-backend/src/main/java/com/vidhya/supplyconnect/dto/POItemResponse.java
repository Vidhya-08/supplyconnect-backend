package com.vidhya.supplyconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class POItemResponse {


    private Long id;
    private Integer lineNumber;
    private String product;
    private String productDescription;
    private Double orderedQuantity;
    private String quantityUom;
    private LocalDate requestedDeliveryDate;
    private Double confirmedQuantity;
    private LocalDate confirmedDeliveryDate;
    private String status;
}