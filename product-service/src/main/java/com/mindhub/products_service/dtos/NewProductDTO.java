package com.mindhub.products_service.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record NewProductDTO(
        @NotBlank(message = "name must be provided and not be blank")
        String name,
        String description,
        @NotNull(message = "price must be provided")
        @PositiveOrZero(message = "price must be a positive value or zero")
        Double price,
        @NotNull(message = "quantity must be provided")
        @PositiveOrZero(message = "quantity must be a positive value or zero")
        Integer stock
) {
}
