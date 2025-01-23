package com.mindhub.products_service.services;

import com.mindhub.products_service.dtos.NewProductDTO;
import com.mindhub.products_service.dtos.PatchProductRequestDTO;
import com.mindhub.products_service.dtos.ProductDTO;
import com.mindhub.products_service.exceptions.InvalidProductException;
import com.mindhub.products_service.exceptions.ProductNotFoundException;
import com.mindhub.products_service.models.Product;

import java.util.List;

public interface ProductService {

    ProductDTO getProductByIdRequest(Long id) throws ProductNotFoundException;

    Product getProductById(Long id) throws ProductNotFoundException;

    List<ProductDTO> getAllProductsRequest();

    List<Product> getAllProducts();

    ProductDTO createNewProductRequest(NewProductDTO newProductDTO);

    Product createNewProduct(NewProductDTO newProductDTO);

    ProductDTO patchProductRequest(Long id, PatchProductRequestDTO patchProduct) throws InvalidProductException, ProductNotFoundException;

    Product patchProduct(Long id, PatchProductRequestDTO patchProduct) throws ProductNotFoundException, InvalidProductException;
}
