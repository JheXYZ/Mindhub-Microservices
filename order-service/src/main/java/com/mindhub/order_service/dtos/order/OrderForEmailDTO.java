package com.mindhub.order_service.dtos.order;

import com.mindhub.order_service.dtos.orderItem.ConfirmOrderItemDTO;
import com.mindhub.order_service.dtos.user.UserDTO;

import java.util.List;

public class OrderForEmailDTO {

    private Long id;

    private UserDTO user;

    private String status;

    private Double totalPrice;

    private List<ConfirmOrderItemDTO> products;

    public OrderForEmailDTO() {
    }

    public OrderForEmailDTO(Long id, UserDTO user, String status, Double totalPrice, List<ConfirmOrderItemDTO> products) {
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

    public List<ConfirmOrderItemDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ConfirmOrderItemDTO> products) {
        this.products = products;
    }
}
