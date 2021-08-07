package com.codecool.stampcollection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.net.URI;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Problem> handleUnsupportedOperation(UnsupportedOperationException exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("/api/unsupported-operation"))
                .withTitle("Unsupported Operation Exception")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

    @ExceptionHandler(CurrencyNotExistsException.class)
    public ResponseEntity<Problem> handleUnsupportedOperation(CurrencyNotExistsException exception) {
        Problem problem = Problem.builder()
                .withType(URI.create("/api/denomination/currency-not-exists"))
                .withTitle("currency not extsts")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(exception.getMessage())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }

}
