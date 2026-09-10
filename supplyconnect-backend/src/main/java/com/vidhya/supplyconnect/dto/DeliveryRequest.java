package com.vidhya.supplyconnect.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRequest {

    @NotNull
    private Long poItemId;

    @NotNull
    @Positive
    private Double receivedQuantity;
}