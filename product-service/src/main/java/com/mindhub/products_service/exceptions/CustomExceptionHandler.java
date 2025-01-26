package com.mindhub.products_service.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {

    public record ErrorResponse(List<String> errors){}

    @ExceptionHandler({InvalidProductException.class, InsufficientProductStockException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse BadRequestExceptionHandler(Exception exception) {
        return response(exception.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse NotFoundExceptionHandler(Exception exception) {
        return response(exception.getMessage());
    }

    // validations handler
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

    // json parser exception or invalid enum
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        if (ex.getCause() == null || !ex.getCause().getMessage().contains("not one of the values accepted for Enum"))
            return response("invalid JSON request");

        String validValues = ex.getMessage().substring(ex.getMessage().indexOf("["));
        return response("invalid value provided; accepted values: " + validValues);
    }

    private ErrorResponse response(String errorMessage) {
        return new ErrorResponse(List.of(errorMessage));
    }

    private ErrorResponse response(List<String> errorResponseList) {
        return new ErrorResponse(errorResponseList);
    }

}
