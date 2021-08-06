package com.codecool.stampcollection.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ItemCommand {

    @NotNull
    private Long denomId;
    @Positive
    @NotNull
    private Long quantity;
    @Positive
    @NotNull
    private Double unitPrice;

    public Long getDenomId() {
        return denomId;
    }

    public void setDenomId(Long denomId) {
        this.denomId = denomId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }
}
