package com.example.ReviewEngine.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.*;


@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    ///
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User createdBy;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @ManyToMany
    @JoinTable(
            name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date createdDate;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Review> reviews = new ArrayList<>();

    public Product() {}


    // Testar builder här
    private Product(Builder builder){
        this.name = builder.name;
        this.category = builder.category;
        this.tags = builder.tags;
        this.reviews = builder.reviews;
        this.createdBy = builder.createdBy;
    }

    public static Builder builder() {
        return new Builder();
    }

    @PrePersist
    protected void onCreate() {
        createdDate = new Date();
    }
    public Date getCreatedDate() {
        return createdDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // builder class

    public static class Builder{
        private String name;
        private String category;
        private Set<Tag> tags = new HashSet<>();
        private List<Review> reviews = new ArrayList<>();
        private User createdBy;

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder category(String category){
            this.category = category;
            return this;
        }

        public Builder tags(Set<Tag> tags){
            this.tags = tags;
            return this;
        }

        public Builder reviews(List<Review> reviews){
            this.reviews = reviews;
            return this;
        }

        public Builder createdBy(User createdBy){
            this.createdBy = createdBy;
            return this;
        }

        public Product build(){
            return new Product(this);
        }
    }

}