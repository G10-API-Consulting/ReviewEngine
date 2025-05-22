package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCustomerIdOrderByNameAsc(Long customerId);
    List<Product> findByCustomerId(Long customerId, Sort sort);
}
