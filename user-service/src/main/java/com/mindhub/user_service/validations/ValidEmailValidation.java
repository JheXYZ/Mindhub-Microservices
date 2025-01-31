package com.mindhub.user_service.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ValidEmailValidation implements ConstraintValidator<ValidEmail, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email == null || email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
