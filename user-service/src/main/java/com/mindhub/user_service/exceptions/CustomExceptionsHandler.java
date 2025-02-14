package com.mindhub.user_service.exceptions;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionsHandler {
    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceptionHandler(Exception exception) {
        return response(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(Exception exception) {
        return response(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidArgument(MethodArgumentNotValidException methodArgumentNotValidException) {
        return response(
                methodArgumentNotValidException
                        .getBindingResult()
                        .getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList()
        );
    }

    @ExceptionHandler(AmqpConnectException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse rabbitMQUnavailableExceptionHandler(AmqpConnectException exception) {
        return response("petition could not be process, try again later");
    }

    // json parse exceptions and invalid enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        if (ex.getCause() == null || !ex.getCause().getMessage().contains("not one of the values accepted for Enum"))
            return response("invalid JSON request");

        String validValues = ex.getMessage().substring(ex.getMessage().indexOf("["));
        if (ex.getMessage().contains("RoleType"))
            return response(
                    "invalid roleType; accepted values: " + validValues);

        return response("invalid value provided; accepted values: " + validValues);
    }

    private ErrorResponse response(String errorMessage) {
        return new ErrorResponse(List.of(errorMessage));
    }

    private ErrorResponse response(List<String> errorResponseList) {
        return new ErrorResponse(errorResponseList);
    }

    public record ErrorResponse(List<String> errors) {
    }
}
