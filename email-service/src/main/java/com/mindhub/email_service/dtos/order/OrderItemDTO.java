package com.mindhub.email_service.dtos.order;

import com.mindhub.email_service.dtos.product.ProductDTO;

public class OrderItemDTO {
    private Long id;

    private Integer quantity;

    private ProductDTO product;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Long id, Integer quantity, ProductDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
