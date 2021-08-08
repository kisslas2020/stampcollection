package com.codecool.stampcollection.model;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum TransactionType {

    BUY("BUY"), SELL("SELL");

    private final String name;


    TransactionType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
