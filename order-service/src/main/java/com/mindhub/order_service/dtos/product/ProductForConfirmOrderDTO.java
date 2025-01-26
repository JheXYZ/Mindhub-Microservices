package com.mindhub.order_service.dtos.product;

public class ProductForConfirmOrderDTO {

    private Long id;

    private String name;

    private Double price;

    public ProductForConfirmOrderDTO() {
    }

    public ProductForConfirmOrderDTO(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductForConfirmOrderDTO(ProductDTO productDTO) {
        this.id = productDTO.id();
        this.name = productDTO.name();
        this.price = productDTO.price();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
