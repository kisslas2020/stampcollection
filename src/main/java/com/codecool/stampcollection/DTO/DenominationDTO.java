package com.codecool.stampcollection.DTO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.Currency;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "value")
public class DenominationDTO {

    private Long id;
    private Double value;
    private Currency currency;
    private Long stampId;
    private Long stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
