package com.mindhub.user_service.exceptions;

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
    public record ErrorResponse(List<String> errors){}

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestExceptionHandler(Exception exception){
        return response(exception.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notFoundExceptionHandler(Exception exception){
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
}
