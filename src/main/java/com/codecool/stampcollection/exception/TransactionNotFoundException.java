package com.codecool.stampcollection.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

public class TransactionNotFoundException extends AbstractThrowableProblem {

    public TransactionNotFoundException(Long id) {
        super(
                URI.create("/api/denomination/transaction-not-found"),
                "not found",
                Status.NOT_FOUND,
                String.format("Transaction with id %d not found", id)
        );
    }
}
