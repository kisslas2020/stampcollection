package com.codecool.stampcollection.exception;

public class DenominationNotFoundException extends RuntimeException{

    public DenominationNotFoundException(Long id) {
        super("Could not find denomination with id: " + id);
    }
}
