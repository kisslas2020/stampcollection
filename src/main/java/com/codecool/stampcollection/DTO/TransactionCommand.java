package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "dateOfTransaction")
public class TransactionCommand {

    @PastOrPresent
    private LocalDate dateOfTransaction;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private Long denomId;
    @Positive
    private Long quantity;
    @Positive
    private Double unitPrice;

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

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
