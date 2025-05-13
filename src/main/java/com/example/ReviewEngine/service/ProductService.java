package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
                .build();

        return productRepository.save(product);
    }


    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}


