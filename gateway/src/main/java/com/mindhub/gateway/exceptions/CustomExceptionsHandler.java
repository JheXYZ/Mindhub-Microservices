package com.mindhub.gateway.exceptions;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class CustomExceptionsHandler {


    @ExceptionHandler(JwtException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse forbiddenHandler(Exception exception) {
        return response(exception.getMessage());
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
