package com.mindhub.order_service.dtos.orderItem;

import com.mindhub.order_service.dtos.product.ProductDTO;
import com.mindhub.order_service.dtos.product.ProductForConfirmOrderDTO;

public class ConfirmOrderItemDTO {

    private Long id;

    private Integer quantity;

    private ProductForConfirmOrderDTO product;

    public ConfirmOrderItemDTO() {
    }

    public ConfirmOrderItemDTO(Long id, Integer quantity, ProductForConfirmOrderDTO product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

    public ConfirmOrderItemDTO(ProductDTO productDTO, Integer quantity) {
        this.id = productDTO.id();
        this.quantity = quantity;
        this.product = new ProductForConfirmOrderDTO(productDTO);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductForConfirmOrderDTO getProduct() {
        return product;
    }

    public void setProduct(ProductForConfirmOrderDTO product) {
        this.product = product;
    }
}
