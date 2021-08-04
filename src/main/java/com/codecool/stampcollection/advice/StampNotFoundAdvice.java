package com.codecool.stampcollection.advice;

import com.codecool.stampcollection.exception.StampNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class StampNotFoundAdvice extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(StampNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String stampNotFoundHandler(StampNotFoundException ex) {
        return ex.getMessage();
    }
}
