package com.codecool.stampcollection.DTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Currency;

public class DenominationCommand {

    @Positive
    @NotNull
    private Double value;
    @NotNull
    private Currency currency;
    @NotNull
    private Long stampId;

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getStampId() {
        return stampId;
    }

    public void setStampId(Long stampId) {
        this.stampId = stampId;
    }

}
