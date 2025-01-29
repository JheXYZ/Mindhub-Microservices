package com.mindhub.order_service.exceptions;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/*@Component*/
public class ErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable error = getError(webRequest);
        errorAttributes.put("error", error.getMessage());
        if (error instanceof MethodArgumentNotValidException)
            errorAttributes.put("error", ((MethodArgumentNotValidException) error).getFieldError().getDefaultMessage());
        if (error instanceof HttpMessageNotReadableException)
            errorAttributes.put("error", error.getLocalizedMessage());

        return errorAttributes;
    }
}
