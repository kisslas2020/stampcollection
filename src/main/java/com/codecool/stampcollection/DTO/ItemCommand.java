package com.codecool.stampcollection.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ItemCommand {

    @NotNull
    private Long denominationId;
    @Positive
    @NotNull
    private Long quantity;
    @Positive
    @NotNull
    private Double unitPrice;
    @NotNull
    private Long transactionId;

    public Long getDenominationId() {
        return denominationId;
    }

    public void setDenominationId(Long denominationId) {
        this.denominationId = denominationId;
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

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}
