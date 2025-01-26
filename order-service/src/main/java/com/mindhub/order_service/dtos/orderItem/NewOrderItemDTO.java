package com.mindhub.order_service.dtos.orderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record NewOrderItemDTO(

        @NotNull(message = "productId must be provided")
        Long productId,

        @NotNull(message = "quantity must be provided")
        @Positive(message = "quantity must be a positive number")
        Integer quantity
) {
}
