package com.mindhub.order_service.dtos.user;

public record UserDTO(
        Long id,
        String username,
        String email
) {
}
