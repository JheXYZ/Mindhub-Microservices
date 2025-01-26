package com.mindhub.products_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PatchProductStockDTO(
        @NotNull(message = "id must be provided")
        Long id,
        @NotNull(message = "quantity must be provided")
        @PositiveOrZero(message = "quantity must be positive or negative")
        Integer quantity
) {
}
