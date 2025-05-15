package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AsyncReviewService;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final AsyncReviewService asyncReviewService;
    private final ProductRepository productRepository;

    public ProductController(AsyncReviewService asyncReviewService, ProductService productService, ProductRepository productRepository){
        this.productService = productService;
        this.asyncReviewService = asyncReviewService;
        this.productRepository = productRepository;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getProducts(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(productService.findByUser(user));
    }


    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal(); // Or your custom UserDetails class
            Product product = productService.createProduct(productRequest, user);

            asyncReviewService.generateAndSaveReviewsAsync(product).join();

            Product updatedProduct = productService.getProductById(product.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
        } catch (Exception e) {
            System.out.println("Fel vid skapande av review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}