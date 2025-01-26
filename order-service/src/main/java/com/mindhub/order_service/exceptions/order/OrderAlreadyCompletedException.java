package com.mindhub.order_service.exceptions.order;

public class OrderAlreadyCompletedException extends Exception{

    public OrderAlreadyCompletedException() {
        super("order is already completed");
    }

    public OrderAlreadyCompletedException(String message) {
        super(message);
    }
}
