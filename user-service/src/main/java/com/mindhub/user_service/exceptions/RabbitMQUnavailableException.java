package com.mindhub.user_service.exceptions;

public class RabbitMQUnavailableException extends Exception {
    public RabbitMQUnavailableException() {
        super("");
    }

    public RabbitMQUnavailableException(String message) {
        super(message);
    }
}
