package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.exception.ProductNotFoundException;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final TagRepository tagRepository;


    public ProductService(ProductRepository productRepository, TagRepository tagRepository){
        this.productRepository = productRepository;
        this.tagRepository = tagRepository;
    }


    public Product createProduct(ProductRequest request){

        Set<Tag> productTags = new HashSet<>();

        for(String tagName : request.getTags()){
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet( () -> tagRepository.save(new Tag(tagName)));
            productTags.add(tag);
        }

        Product product = Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .tags(productTags)
                .customerId(request.getCustomerId())
                .build();

        return productRepository.save(product);
    }


    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produkt med id " + id + " finns inte."));

    }


    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void deleteProduct(Long id){
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Produkt med id " + id + " finns inte."));
        productRepository.delete(existingProduct);
    }

    public void calculateAverageRating(Product product){
        float tempRating = 0;
        int i = product.getReviews().size();
        if(i > 0) {
            for (Review review : product.getReviews()) {
                tempRating += review.getRating();
            }
            tempRating /= i;
            float rounded = Math.round(tempRating * 100) / 100f;
            product.setRating(rounded);
        }
        else {
            product.setRating(0);
        }
    }

    public List<Product> getProductsForCustomer(Long customerId, Sort sort) {
        return productRepository.findByCustomerId(customerId, sort);
    }
}

