package com.mindhub.user_service.exceptions;

public class InvalidUserException extends Exception{

    public InvalidUserException() {
        super("invalid user");
    }

    public InvalidUserException(String message) {
        super(message);
    }
}
