package com.mindhub.products_service.repositories;

import com.mindhub.products_service.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
