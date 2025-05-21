package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AsyncReviewService;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.exception.ProductNotFoundException;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final AsyncReviewService asyncReviewService;

    public ProductController(AsyncReviewService asyncReviewService, ProductService productService ){
        this.productService = productService;
        this.asyncReviewService = asyncReviewService;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }


    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody ProductRequest productRequest){
        try {
            Product product = productService.createProduct(productRequest);

            asyncReviewService.generateAndSaveReviewsAsync(product).join();

            Product updatedProduct = productService.getProductById(product.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
        } catch (Exception e) {

            System.out.println("Fel vid skapande av review: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
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