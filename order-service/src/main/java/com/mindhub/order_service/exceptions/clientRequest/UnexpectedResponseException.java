package com.mindhub.order_service.exceptions.clientRequest;

public class UnexpectedResponseException extends Exception {

    public UnexpectedResponseException() {
        super("unexpected response");
    }

    public UnexpectedResponseException(String message) {
        super(message);
    }
}
