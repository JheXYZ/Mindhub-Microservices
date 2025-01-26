package com.mindhub.products_service.exceptions;

public class InsufficientProductStockException extends Exception{

    public InsufficientProductStockException() {
        super("not enough quantity available");
    }

    public InsufficientProductStockException(String message) {
        super(message);
    }
}
