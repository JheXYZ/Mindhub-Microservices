package com.mindhub.user_service.dtos;

import com.mindhub.user_service.validations.NoWhitespaces;
import com.mindhub.user_service.validations.ValidEmail;
import jakarta.validation.constraints.NotNull;

public record LoginUserDTO(
        @NotNull(message = "email must be provided")
        @ValidEmail
        String email,
        @NotNull(message = "password must be provided")
        @NoWhitespaces(message = "invalid password")
        String password
) {
}
