package com.mindhub.order_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*@ResponseStatus(HttpStatus.BAD_REQUEST)*/
public class InvalidOrderException extends Exception{

    public InvalidOrderException() {
        super("invalid order");
    }

    public InvalidOrderException(String message) {
        super(message);
    }
}
