package com.mindhub.user_service.dtos;

import com.mindhub.user_service.models.RoleType;
import com.mindhub.user_service.validations.NoWhitespaces;
import com.mindhub.user_service.validations.UniqueEmail;
import com.mindhub.user_service.validations.ValidEmail;
import org.hibernate.validator.constraints.Length;


public record PatchUserRequestDTO(

        @NoWhitespaces(message = "username can not contain whitespaces")
        String username,
        @ValidEmail
        @UniqueEmail
        String email,
        @Length(min = 6, max = 40)
        @NoWhitespaces(message = "password can not contain whitespaces")
        String password,
        RoleType role
) {
}
