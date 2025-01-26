package com.mindhub.order_service.dtos.product;

public record ProductDTO (
        Long id,
        String name,
        String description,
        Double price,
        Integer stock
) {
}
