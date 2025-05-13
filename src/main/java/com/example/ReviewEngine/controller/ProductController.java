package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AIClient;
import com.example.ReviewEngine.dto.ProductIdRequest;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ProductService;
import com.example.ReviewEngine.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final ReviewService reviewService;
    private final AIClient aiClient;

    public ProductController(ProductService productService, AIClient aiClient, ReviewService reviewService ){
        this.productService = productService;
        this.aiClient = aiClient;
        this.reviewService = reviewService;
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest){
        try {
            Product product = productService.createProduct(productRequest);

            Review review = aiClient.generateReview(product);

            ReviewRequest reviewRequest = ReviewRequest.builder()
                    .productId(product.getProductId())
                    .reviewerName(review.getReviewerName())
                    .text(review.getText())
                    .rating(review.getRating())
                    .build();

            for (int i = 0; i < 5; i++) {
                reviewService.createReview(reviewRequest);
            }

            Product updatedProduct = productService.getProductById(product.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedProduct);
        } catch (Exception e) {

            System.out.println("Fel vid skapande av review: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/id")
    public ResponseEntity<Product> getProduct(@RequestBody ProductIdRequest request){
        return ResponseEntity.ok(productService.getProductById(request.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

}