package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.dtos.orderItem.ConfirmOrderItemDTO;
import com.mindhub.order_service.models.order.Order;
import com.mindhub.order_service.models.order.OrderStatus;

import java.util.List;

public class ConfirmOrderDTO {

    private Long id;

    private Long userId;

    private OrderStatus status;

    private Double totalPrice;

    private List<ConfirmOrderItemDTO> products;

    public ConfirmOrderDTO() {
    }

    public ConfirmOrderDTO(Long id, Long userId, OrderStatus status, Double totalPrice, List<ConfirmOrderItemDTO> products) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public ConfirmOrderDTO(Order order, List<ConfirmOrderItemDTO> products) {
        this.id = order.getId();
        this.userId = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.products = products;
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

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ConfirmOrderItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ConfirmOrderItemDTO> products) {
        this.products = products;
    }
}
