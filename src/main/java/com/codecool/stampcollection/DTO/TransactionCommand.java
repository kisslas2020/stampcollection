package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.model.TransactionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ApiModel
public class TransactionCommand {

    @PastOrPresent
    @NotNull
    private LocalDate dateOfTransaction;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty
    @NotNull
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
