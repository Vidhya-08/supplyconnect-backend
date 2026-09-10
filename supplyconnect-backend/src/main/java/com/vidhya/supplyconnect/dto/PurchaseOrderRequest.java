package com.vidhya.supplyconnect.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderRequest {

    @NotNull
    private Long supplierId;

    @NotNull
    private Long siteId;

    @NotNull
    private LocalDate poDate;

    @NotNull
    @Valid
    private List<POItemRequest> items;
}