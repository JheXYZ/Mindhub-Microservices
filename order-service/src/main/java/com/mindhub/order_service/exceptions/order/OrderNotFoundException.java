package com.mindhub.order_service.exceptions.order;

/*@ResponseStatus(HttpStatus.NOT_FOUND)*/
public class OrderNotFoundException extends Exception {

    public OrderNotFoundException() {
        super("order not found");
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
