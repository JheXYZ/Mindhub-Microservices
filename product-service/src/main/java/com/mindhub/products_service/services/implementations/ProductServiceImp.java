package com.mindhub.products_service.services.implementations;

import com.mindhub.products_service.dtos.NewProductDTO;
import com.mindhub.products_service.dtos.PatchProductRequestDTO;
import com.mindhub.products_service.dtos.ProductDTO;
import com.mindhub.products_service.exceptions.InvalidProductException;
import com.mindhub.products_service.exceptions.ProductNotFoundException;
import com.mindhub.products_service.models.Product;
import com.mindhub.products_service.repositories.ProductRepository;
import com.mindhub.products_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductDTO> getAllProductsRequest() {
        return getAllProducts().stream().map(ProductDTO::new).toList();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductDTO createNewProductRequest(NewProductDTO newProductDTO) {
        return new ProductDTO(createNewProduct(newProductDTO));
    }

    @Override
    public Product createNewProduct(NewProductDTO newProductDTO) {
        return productRepository.save(
                new Product(
                    newProductDTO.name(),
                    newProductDTO.description(),
                    newProductDTO.price(),
                    newProductDTO.stock())
        );
    }

    @Override
    public ProductDTO patchProductRequest(Long id, PatchProductRequestDTO patchProduct) throws InvalidProductException, ProductNotFoundException {
        return new ProductDTO(patchProduct(id, patchProduct));
    }

    @Override
    public Product patchProduct(Long id, PatchProductRequestDTO patchProduct) throws ProductNotFoundException, InvalidProductException {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        makePatchUpdates(product, patchProduct);
        return product;
    }

    private void makePatchUpdates(Product product, PatchProductRequestDTO patchProduct) throws InvalidProductException {
        if (patchProduct == null || patchProduct.name() == null && patchProduct.description() == null && patchProduct.price() == null && patchProduct.stock() == null)
            throw new InvalidProductException("at least one field must be provided for update");

        if (patchProduct.stock() != null)
            product.setStock(patchProduct.stock());
        if (patchProduct.price() != null)
            product.setPrice(patchProduct.price());
        if (patchProduct.name() != null && !patchProduct.name().isBlank())
            product.setName(patchProduct.name());
        if (patchProduct.description() != null && !patchProduct.description().isBlank())
            product.setDescription(patchProduct.description());
    }

    @Override
    public ProductDTO getProductByIdRequest(Long id) throws ProductNotFoundException {
        return new ProductDTO(getProductById(id));
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }
}
