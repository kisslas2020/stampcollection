package com.codecool.stampcollection.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import java.net.URI;

public class DenominationNotFoundException extends AbstractThrowableProblem {

    public DenominationNotFoundException(Long id) {
        super(
                URI.create("/api/denomination/denomination-not-found"),
                "not found",
                Status.NOT_FOUND,
                String.format("Denomination with id %d not found", id)
        );
    }
}
