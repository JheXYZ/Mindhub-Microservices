package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.dtos.orderItem.OrderItemForOrderDTO;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.order.OrderStatus;

import java.util.List;

public class OrderDTO {

    private Long id;

    private Long userId;

    private OrderStatus status;

    private List<OrderItemForOrderDTO> products;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Long userId, OrderStatus status, List<OrderItemForOrderDTO> products) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.products = products;
    }

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.status = order.getStatus();
        this.products = order.getProducts().stream().map(OrderItemForOrderDTO::new).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItemForOrderDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItemForOrderDTO> products) {
        this.products = products;
    }

}
