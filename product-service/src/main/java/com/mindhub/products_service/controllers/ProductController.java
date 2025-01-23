package com.mindhub.products_service.controllers;

import com.mindhub.products_service.dtos.NewProductDTO;
import com.mindhub.products_service.dtos.PatchProductRequestDTO;
import com.mindhub.products_service.dtos.ProductDTO;
import com.mindhub.products_service.exceptions.InvalidProductException;
import com.mindhub.products_service.exceptions.ProductNotFoundException;
import com.mindhub.products_service.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProductsRequest();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.getProductByIdRequest(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createNewProduct(@Valid @RequestBody NewProductDTO newProductDTO){
        return productService.createNewProductRequest(newProductDTO);
    }

    @PatchMapping("/{id}")
    public ProductDTO patchProduct(@PathVariable Long id, @Valid @RequestBody PatchProductRequestDTO patchProduct) throws InvalidProductException, ProductNotFoundException {
        return productService.patchProductRequest(id, patchProduct);
    }

}
