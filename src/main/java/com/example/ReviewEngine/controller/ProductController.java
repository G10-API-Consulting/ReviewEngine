package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.ProductIdRequest;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductRepository repo;

    public ProductController(ProductRepository repo){
        this.repo = repo;
    }

    @PostMapping("id")
    public ResponseEntity<Product> getProduct(@RequestBody ProductIdRequest productRequest) throws Exception{
        Product product = repo.findById(productRequest.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return ResponseEntity.ok(product);
    }

    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@RequestBody ProductRequest productRequest){
        Product product = new Product(productRequest.getName(), productRequest.getCategory(), productRequest.getTags());
        return ResponseEntity.status(HttpStatus.CREATED).body(repo.save(product));
    }

}
