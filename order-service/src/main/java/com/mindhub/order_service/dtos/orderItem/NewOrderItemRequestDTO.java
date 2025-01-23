package com.mindhub.order_service.dtos.orderItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record NewOrderItemRequestDTO(
        @NotNull(message = "orderId must be provided")
        Long orderId,
        @NotNull(message = "productId must be provided")
        Long productId,
        @NotNull(message = "quantity must be provided")
        @PositiveOrZero(message = "quantity must be positive or zero")
        Integer quantity
) {
}
