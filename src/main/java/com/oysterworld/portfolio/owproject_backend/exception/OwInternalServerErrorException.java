package com.oysterworld.portfolio.owproject_backend.exception;

import org.springframework.http.HttpStatus;

import com.oysterworld.framework.http.process.exception.ResponseHandlerException;

public class OwInternalServerErrorException extends ResponseHandlerException {
    public OwInternalServerErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }
}
