package com.mindhub.products_service.controllers;

import com.mindhub.products_service.dtos.NewProductDTO;
import com.mindhub.products_service.dtos.PatchProductRequestDTO;
import com.mindhub.products_service.dtos.PatchProductStockDTO;
import com.mindhub.products_service.dtos.ProductDTO;
import com.mindhub.products_service.exceptions.InsufficientProductStockException;
import com.mindhub.products_service.exceptions.InvalidProductException;
import com.mindhub.products_service.exceptions.ProductNotFoundException;
import com.mindhub.products_service.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @GetMapping(params = "ids")
    public List<ProductDTO> getProductsListById(@RequestParam(name = "ids") Set<Long> ids) throws ProductNotFoundException {
        return productService.getProductsListByIdsRequest(ids);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductById(@PathVariable Long id) throws ProductNotFoundException {
        return productService.getProductByIdRequest(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createNewProduct(@Valid @RequestBody NewProductDTO newProductDTO) {
        return productService.createNewProductRequest(newProductDTO);
    }

    @PatchMapping("/{id}")
    public ProductDTO patchProduct(@PathVariable Long id, @Valid @RequestBody PatchProductRequestDTO patchProduct) throws InvalidProductException, ProductNotFoundException {
        return productService.patchProductRequest(id, patchProduct);
    }

    @PatchMapping("/stock")
    public List<ProductDTO> patchProductsStock(@RequestBody List<PatchProductStockDTO> patchProductStockDTOS) throws InsufficientProductStockException, ProductNotFoundException {
        return productService.deductStockRequest(patchProductStockDTOS);
    }


}
