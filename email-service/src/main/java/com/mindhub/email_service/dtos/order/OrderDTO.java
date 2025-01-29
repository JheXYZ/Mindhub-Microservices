package com.mindhub.email_service.dtos.order;

import com.mindhub.email_service.dtos.user.UserDTO;

import java.util.List;

public class OrderDTO {
    private Long id;

    private UserDTO user;

    private String status;

    private Double totalPrice;

    private List<OrderItemDTO> products;

    public OrderDTO() {
    }

    public OrderDTO(Long id, UserDTO user, String status, Double totalPrice, List<OrderItemDTO> products) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.totalPrice = totalPrice;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderItemDTO> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", email='" + user + '\'' +
                ", status='" + status + '\'' +
                ", totalPrice=" + totalPrice +
                ", products=" + products +
                '}';
    }
}
