package com.mindhub.order_service.exceptions.clientRequest;

import org.springframework.web.bind.annotation.ResponseStatus;

public class UnexpectedResponseException extends Exception{

    public UnexpectedResponseException() {
        super("unexpected response");
    }

    public UnexpectedResponseException(String message) {
        super(message);
    }
}
