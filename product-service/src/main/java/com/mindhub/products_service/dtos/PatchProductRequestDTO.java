package com.mindhub.products_service.dtos;

import jakarta.validation.constraints.PositiveOrZero;

public record PatchProductRequestDTO(
        String name,
        String description,
        @PositiveOrZero(message = "price must be positive or zero")
        Double price,
        @PositiveOrZero(message = "quantity must be positive or zero")
        Integer stock
) {
}
