package com.mindhub.email_service.dtos.user;

public record UserDTO(
        Long id,
        String email,
        String username
) {
}
