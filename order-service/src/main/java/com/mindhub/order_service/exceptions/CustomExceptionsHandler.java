package com.mindhub.order_service.exceptions;

import com.mindhub.order_service.exceptions.clientRequest.UnexpectedResponseException;
import com.mindhub.order_service.exceptions.clientRequest.UnexpectedValueException;
import com.mindhub.order_service.exceptions.order.InvalidOrderException;
import com.mindhub.order_service.exceptions.order.OrderAlreadyCompletedException;
import com.mindhub.order_service.exceptions.order.OrderItemNotFoundException;
import com.mindhub.order_service.exceptions.order.OrderNotFoundException;
import com.mindhub.order_service.exceptions.product.InsufficientProductStockException;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.exceptions.user.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionsHandler.class);

    @ExceptionHandler({OrderNotFoundException.class, OrderItemNotFoundException.class, UserNotFoundException.class, ProductNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(Exception exception) {
        return response(exception.getMessage());
    }

    @ExceptionHandler({InvalidOrderException.class, InsufficientProductStockException.class, OrderAlreadyCompletedException.class, UnexpectedValueException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
        return response(exception.getMessage());
    }

    @ExceptionHandler(UnexpectedResponseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerErrorExceptionHandler(Exception exception) {
        log.error(exception.getMessage());
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

    // json parse exceptions and invalid enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        if (ex.getCause() == null || !ex.getCause().getMessage().contains("not one of the values accepted for Enum"))
            return response("invalid JSON request");

        String validValues = ex.getMessage().substring(ex.getMessage().indexOf("["));
        if (ex.getMessage().contains("OrderStatus"))
            return response(
                    "invalid status; accepted values: " + validValues);

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