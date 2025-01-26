package com.mindhub.order_service.exceptions.user;

public class UserNotFoundException extends Exception{

    public UserNotFoundException() {
        super("user not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
