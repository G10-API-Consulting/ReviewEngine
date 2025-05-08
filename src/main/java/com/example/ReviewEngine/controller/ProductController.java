package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AIClient;
import com.example.ReviewEngine.dto.ProductIdRequest;
import com.example.ReviewEngine.dto.ProductRequest;

import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final AIClient aiClient;

    public ProductController(ProductService productService, AIClient aiClient ){
        this.productService = productService;
        this.aiClient = aiClient;
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest){
        Product product = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PostMapping("/id")
    public ResponseEntity<Product> getProduct(@RequestBody ProductIdRequest request){
        return ResponseEntity.ok(productService.getProductById(request.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        aiClient.generateReview();
        return ResponseEntity.ok(productService.getAllProducts());
    }

}