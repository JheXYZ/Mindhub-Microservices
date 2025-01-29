package com.mindhub.order_service.exceptions.order;

/*@ResponseStatus(HttpStatus.BAD_REQUEST)*/
public class InvalidOrderException extends Exception {

    public InvalidOrderException() {
        super("invalid order");
    }

    public InvalidOrderException(String message) {
        super(message);
    }
}
