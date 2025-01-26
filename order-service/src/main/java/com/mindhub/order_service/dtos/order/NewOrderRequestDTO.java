package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.dtos.orderItem.NewOrderItemDTO;
import com.mindhub.order_service.models.order.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record NewOrderRequestDTO(
        @NotNull(message = "userEmail must be provided")
        String userEmail,
/*        @NotNull(message = "status must be provided")
        OrderStatus status,*/
        @NotEmpty(message = "products must be provided")
        @Valid
        List<NewOrderItemDTO> products
) {
}
