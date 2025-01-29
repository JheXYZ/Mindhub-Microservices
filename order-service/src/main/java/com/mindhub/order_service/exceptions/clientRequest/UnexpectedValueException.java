package com.mindhub.order_service.exceptions.clientRequest;

public class UnexpectedValueException extends Exception {

    public UnexpectedValueException() {
        super("unexpected value");
    }

    public UnexpectedValueException(String message) {
        super(message);
    }
}
