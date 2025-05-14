package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
