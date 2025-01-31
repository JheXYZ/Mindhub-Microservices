package com.mindhub.user_service.dtos;

import com.mindhub.user_service.validations.NoWhitespaces;
import com.mindhub.user_service.validations.UniqueEmail;
import com.mindhub.user_service.validations.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterUserDTO(
        @NotBlank(message = "email must be provided")
        @ValidEmail
        @UniqueEmail
        String email,
        @NotNull(message = "password must be provided")
        @NoWhitespaces(message = "password can not contain whitespaces")
        @Length(min = 6, max = 40, message = "password must have between 6 and 40 characters")
        String password,
        @NotBlank(message = "username must be provided")
        @NoWhitespaces(message = "username can not contain whitespaces")
        String username
) {
}
