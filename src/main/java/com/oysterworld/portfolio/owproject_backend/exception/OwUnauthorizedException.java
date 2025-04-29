package com.oysterworld.portfolio.owproject_backend.exception;

import org.springframework.http.HttpStatus;

import com.oysterworld.framework.http.process.exception.ResponseHandlerException;

public class OwUnauthorizedException extends ResponseHandlerException{
    public OwUnauthorizedException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }

    public OwUnauthorizedException(String message, String val) {
        super(HttpStatus.BAD_REQUEST.value(), String.format(message + " Input Value : \"%s\"", val));
    }
}
