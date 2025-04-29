package com.oysterworld.portfolio.owproject_backend.exception;

import org.springframework.http.HttpStatus;

import com.oysterworld.framework.http.process.exception.ResponseHandlerException;

public class OwBadRequestException extends ResponseHandlerException {

    public OwBadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public OwBadRequestException(String message, String val) {
        super(HttpStatus.BAD_REQUEST.value(), String.format(message + " Input Value : \"%s\"", val));
    }
}
