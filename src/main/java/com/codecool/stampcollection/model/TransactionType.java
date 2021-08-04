package com.codecool.stampcollection.model;

public enum TransactionType {

    BUY("BUY"), SELL("SELL");

    private final String name;

    TransactionType(String name) {
        this.name = name;
    }
}
