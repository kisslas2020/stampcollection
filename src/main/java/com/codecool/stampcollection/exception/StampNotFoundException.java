package com.codecool.stampcollection.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class StampNotFoundException extends AbstractThrowableProblem {

    public StampNotFoundException(Long id) {
        super(
                URI.create("/api/denomination/stamp-not-found"),
                "not found",
                Status.NOT_FOUND,
                String.format("Stamp with id %d not found", id)
        );
    }
}
