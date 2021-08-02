package com.codecool.stampcollection.exception;

public class StampNotFoundException extends RuntimeException{

    public StampNotFoundException(Long id) {
        super("Could not find stamp with id: " + id);
    }
}
