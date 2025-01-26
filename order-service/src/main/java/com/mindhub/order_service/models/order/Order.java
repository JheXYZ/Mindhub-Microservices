package com.mindhub.order_service.models.order;

import com.mindhub.order_service.dtos.product.ProductDTO;
import com.mindhub.order_service.exceptions.product.ProductNotFoundException;
import com.mindhub.order_service.models.orderItem.OrderItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "user id can not be null")
    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    private Double totalPrice;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> products = new ArrayList<>();

    public Order() {
    }

    public Order(Long userId, Double totalPrice, OrderStatus status) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Order(Long userId, OrderStatus orderStatus, Double totalPrice, List<OrderItem> products) {
        this.userId = userId;
        this.status = orderStatus;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Long getId() {
        return id;
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

    public List<OrderItem> getOrderItems() {
        return products;
    }

    public void setOrderItems(List<OrderItem> products) {
        this.products = products;
    }

    public List<Long> getProducts() {
        return products.stream().map(OrderItem::getProductId).toList();
    }

    public List<OrderItem> addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
        this.products.add(orderItem);
        return this.products;
    }

    public void clearProducts(){
        this.products.clear();
    }
}
