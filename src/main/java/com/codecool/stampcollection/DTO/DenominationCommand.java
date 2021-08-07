package com.codecool.stampcollection.DTO;

import com.codecool.stampcollection.validator.CurrencyConstraint;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Currency;

public class DenominationCommand {

    @Positive
    @NotNull
    private Double value;
    @NotNull
    @CurrencyConstraint(message = "Use three-letter currency codes")
    private String currency;
    @NotNull
    private Long stampId;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getStampId() {
        return stampId;
    }

    public void setStampId(Long stampId) {
        this.stampId = stampId;
    }

}
