package com.mindhub.products_service.repositories;

import com.mindhub.products_service.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
