package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.dtos.orderItem.NewOrderItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record NewOrderRequestFromUser(
        @NotEmpty(message = "products must be provided")
        @Valid
        List<NewOrderItemDTO> products
) {
}
