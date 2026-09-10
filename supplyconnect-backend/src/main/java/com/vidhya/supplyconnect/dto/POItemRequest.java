package com.vidhya.supplyconnect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class POItemRequest {

    @NotNull
    private Integer lineNumber;

    @NotBlank
    private String product;

    private String productDescription;

    @Positive
    @NotNull
    private Double orderedQuantity;

    @NotBlank
    private String quantityUom;

    @NotNull
    private LocalDate requestedDeliveryDate;

    public @NotNull Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(@NotNull Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public @NotBlank String getProduct() {
        return product;
    }

    public void setProduct(@NotBlank String product) {
        this.product = product;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public @Positive @NotNull Double getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(@Positive @NotNull Double orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public @NotBlank String getQuantityUom() {
        return quantityUom;
    }

    public void setQuantityUom(@NotBlank String quantityUom) {
        this.quantityUom = quantityUom;
    }

    public @NotNull LocalDate getRequestedDeliveryDate() {
        return requestedDeliveryDate;
    }

    public void setRequestedDeliveryDate(@NotNull LocalDate requestedDeliveryDate) {
        this.requestedDeliveryDate = requestedDeliveryDate;
    }
}
