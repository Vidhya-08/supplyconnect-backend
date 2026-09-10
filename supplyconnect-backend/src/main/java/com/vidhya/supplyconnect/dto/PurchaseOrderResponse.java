package com.vidhya.supplyconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {

    private Long id;
    private String poNumber;
    private String buyer;
    private String supplier;
    private String site;
    private LocalDate poDate;
    private String status;
    private List<POItemResponse> items;
}