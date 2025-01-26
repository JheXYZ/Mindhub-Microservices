package com.mindhub.products_service.services;

import com.mindhub.products_service.dtos.NewProductDTO;
import com.mindhub.products_service.dtos.PatchProductRequestDTO;
import com.mindhub.products_service.dtos.PatchProductStockDTO;
import com.mindhub.products_service.dtos.ProductDTO;
import com.mindhub.products_service.exceptions.InsufficientProductStockException;
import com.mindhub.products_service.exceptions.InvalidProductException;
import com.mindhub.products_service.exceptions.ProductNotFoundException;
import com.mindhub.products_service.models.Product;

import java.util.List;
import java.util.Set;

public interface ProductService {

    ProductDTO getProductByIdRequest(Long id) throws ProductNotFoundException;

    Product getProductById(Long id) throws ProductNotFoundException;

    List<ProductDTO> getAllProductsRequest();

    List<Product> getAllProducts();

    ProductDTO createNewProductRequest(NewProductDTO newProductDTO);

    Product createNewProduct(NewProductDTO newProductDTO);

    ProductDTO patchProductRequest(Long id, PatchProductRequestDTO patchProduct) throws InvalidProductException, ProductNotFoundException;

    Product patchProduct(Long id, PatchProductRequestDTO patchProduct) throws ProductNotFoundException, InvalidProductException;

    List<ProductDTO> getProductsListByIdsRequest(Set<Long> idList) throws ProductNotFoundException;

    List<Product> getProductsListByIds(Set<Long> idList) throws ProductNotFoundException;

    List<ProductDTO> deductStockRequest(List<PatchProductStockDTO> patchProductStockDTOS) throws InsufficientProductStockException, ProductNotFoundException;

    List<Product> deductStock(List<PatchProductStockDTO> patchProductStockDTOS) throws ProductNotFoundException, InsufficientProductStockException;
}
