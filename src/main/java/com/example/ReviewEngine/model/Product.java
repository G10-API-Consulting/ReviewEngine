package com.example.ReviewEngine.model;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String category;

    @ElementCollection
    private List<String>  tags = new ArrayList<>();

    @Column(nullable = false)
    private Date createdDate;

    private String productUrl;

    public Product() {
    }

    public Product(String name, String category, List<String>tags){
        this.name = name;
        this.category = category;
        this.tags = tags;
    }


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long produtcId) {
        this.productId = produtcId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
