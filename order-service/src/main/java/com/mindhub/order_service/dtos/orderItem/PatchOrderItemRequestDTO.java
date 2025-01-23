package com.mindhub.order_service.dtos.orderItem;

import jakarta.validation.constraints.PositiveOrZero;

public record PatchOrderItemRequestDTO(

        Long productId,

        Long orderId,

        @PositiveOrZero(message = "quantity must be positive or zero")
        Integer quantity
) {
}
