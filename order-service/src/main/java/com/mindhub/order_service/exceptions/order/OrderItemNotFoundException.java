package com.mindhub.order_service.exceptions.order;

/*@ResponseStatus(HttpStatus.NOT_FOUND)*/
public class OrderItemNotFoundException extends Exception {

    public OrderItemNotFoundException() {
        super("orderItem not found");
    }

    public OrderItemNotFoundException(String message) {
        super(message);
    }
}
