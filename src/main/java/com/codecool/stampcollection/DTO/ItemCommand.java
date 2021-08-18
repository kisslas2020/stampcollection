package com.codecool.stampcollection.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ItemCommand {

    @NotNull(message = "Denomination id must not be null")
    private Long denominationId;
    @Positive(message = "Quantity must be greater than 0")
    @NotNull(message = "Quantity must not be null")
    private Long quantity;
    @Positive(message = "Unit price must be greater than 0")
    @NotNull(message = "Unit price must not be null")
    private Double unitPrice;
    @NotNull(message = "Transaction id must not be null")
    private Long transactionId;

    public ItemCommand() {
    }

    public ItemCommand(Long denominationId, Long quantity, Double unitPrice, Long transactionId) {
        this.denominationId = denominationId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.transactionId = transactionId;
    }

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
