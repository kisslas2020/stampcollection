package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.TransactionType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

public class TransactionCommand {

    @PastOrPresent
    private LocalDate dateOfTransaction;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

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

}
