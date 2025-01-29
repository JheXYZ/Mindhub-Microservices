package com.mindhub.products_service.exceptions;

public class InvalidProductException extends Exception {
    public InvalidProductException() {
        super("invalid product");
    }

    public InvalidProductException(String message) {
        super(message);
    }
}
