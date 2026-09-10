package com.vidhya.supplyconnect.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ASNRequest {

    @NotNull
    private Long poItemId;

    @NotNull
    @Positive
    private Double shippedQuantity;

    @NotNull
    private LocalDate shipmentDate;
}
