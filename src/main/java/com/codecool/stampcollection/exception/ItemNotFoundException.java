package com.codecool.stampcollection.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class ItemNotFoundException extends AbstractThrowableProblem {

    public ItemNotFoundException(Long id) {
        super(
                URI.create("/api/denomination/item-not-found"),
                "not found",
                Status.NOT_FOUND,
                String.format("Item with id %d not found", id)
        );
    }
}
