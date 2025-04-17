package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    @PostMapping
    public ResponseEntity<String> getProduct(@RequestBody Product product){
        return ResponseEntity.ok("Product : " + product.getName());
    }
}
