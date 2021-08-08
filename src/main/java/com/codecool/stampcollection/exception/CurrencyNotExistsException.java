package com.codecool.stampcollection.exception;

public class CurrencyNotExistsException extends RuntimeException{

    public CurrencyNotExistsException(String currency) {
        super("Currency does not exists with code: " +  currency);
    }
}
