package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.validator.TransactionTypeConstraint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ApiModel
public class TransactionCommand {

    @PastOrPresent
    @NotNull
    private LocalDate dateOfTransaction;

    @ApiModelProperty
    @NotNull
    @TransactionTypeConstraint
    private String transactionType;

    public LocalDate getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(LocalDate dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
