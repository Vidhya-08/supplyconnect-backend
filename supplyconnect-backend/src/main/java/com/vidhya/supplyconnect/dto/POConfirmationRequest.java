package com.vidhya.supplyconnect.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class POConfirmationRequest {

    @NotNull
    @PositiveOrZero
    private Double confirmedQuantity;

    @NotNull
    private LocalDate confirmedDeliveryDate;
}