package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.models.order.OrderStatus;
import jakarta.validation.constraints.PositiveOrZero;

public record PatchOrderRequestDTO(

        @PositiveOrZero(message = "invalid userId")
        Long userId,

        OrderStatus status
) {
}
