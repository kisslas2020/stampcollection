package com.codecool.stampcollection.model;

import javax.persistence.*;
import java.util.Currency;

@Entity
public class Denomination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double value;
    private Currency currency;
    @ManyToOne
    private Stamp stamp;
    private Long stock;

    public Denomination() {
    }

    public Denomination(Long id, Double value, Currency currency, Stamp stamp, Long stock) {
        this.id = id;
        this.value = value;
        this.currency = currency;
        this.stamp = stamp;
        this.stock = stock;
    }

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

    public Stamp getStamp() {
        return stamp;
    }

    public void setStamp(Stamp stamp) {
        this.stamp = stamp;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}
