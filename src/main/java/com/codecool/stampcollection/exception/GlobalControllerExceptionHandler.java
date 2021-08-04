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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Problem> handleJsonParseError(HttpMessageNotReadableException hmne) {
        Problem problem = Problem.builder()
                .withType(URI.create("/api/json-parse-error"))
                .withTitle("JSON parse error")
                .withStatus(Status.BAD_REQUEST)
                .withDetail(hmne.getCause().getLocalizedMessage().split("\\n")[0])
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
