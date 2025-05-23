package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AsyncReviewService;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.CityLoader;
import com.example.ReviewEngine.service.ProductService;
import com.example.ReviewEngine.service.WeatherClient;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final AsyncReviewService asyncReviewService;
    private final CityLoader cityLoader;
    private final WeatherClient weatherClient;

    public ProductController(AsyncReviewService asyncReviewService, ProductService productService, CityLoader cityLoader, WeatherClient weatherClient ){
        this.productService = productService;
        this.asyncReviewService = asyncReviewService;
        this.cityLoader = cityLoader;
        this.weatherClient = weatherClient;
    }


    @GetMapping
    public ResponseEntity<List<Product>> getProducts(){
        List<Product> products = productService.getAllProducts();

        for(Product product : products){
            productService.calculateAverageRating(product);
        }

        return ResponseEntity.ok(products);
    }


    @PostMapping("/save")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody ProductRequest productRequest){
        try {

            List<String> description = new ArrayList<>();
            for (int i = 0; i <= 4; i++){
                description.add(weatherClient.getCurrentWeather());
            }

            Product product = productService.createProduct(productRequest);

            asyncReviewService.generateAndSaveReviewsAsync(product, description).join();


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
        Product product = productService.getProductById(id);
        productService.calculateAverageRating(product);
        return ResponseEntity.ok(product);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<Product>> getProductsForCustomer(
            @PathVariable("id") Long customerId,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        List<Product> sorted = productService.getProductsForCustomer(customerId, sort);
        for(Product product : sorted){
            productService.calculateAverageRating(product);
        }
        return ResponseEntity.ok(sorted);
    }

}