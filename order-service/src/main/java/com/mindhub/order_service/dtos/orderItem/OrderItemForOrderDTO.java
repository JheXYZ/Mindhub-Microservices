package com.mindhub.order_service.dtos.orderItem;


import com.mindhub.order_service.dtos.product.ProductDTO;
import com.mindhub.order_service.models.orderItem.OrderItem;

public class OrderItemForOrderDTO{

    private Long id;

    private Long productId;

    private Integer quantity;

    public OrderItemForOrderDTO(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItemForOrderDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.productId = orderItem.getProductId();
        this.quantity = orderItem.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
