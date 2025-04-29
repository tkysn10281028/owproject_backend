package com.oysterworld.portfolio.owproject_backend.exception;

import org.springframework.http.HttpStatus;

import com.oysterworld.framework.http.process.exception.ResponseHandlerException;

public class OwForbiddenException extends ResponseHandlerException {

    public OwForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN.value(), message);
    }

    public OwForbiddenException(String message, String val) {
        super(HttpStatus.FORBIDDEN.value(), String.format(message + " Input Value : \"%s\"", val));
    }
}
