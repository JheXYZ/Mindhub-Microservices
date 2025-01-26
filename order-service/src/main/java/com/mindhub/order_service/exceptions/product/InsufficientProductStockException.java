package com.mindhub.order_service.exceptions.product;

public class InsufficientProductStockException extends Exception{

    public InsufficientProductStockException() {
        super("not enough stock available");
    }

    public InsufficientProductStockException(String message) {
        super(message);
    }
}
