package com.example.ReviewEngine.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@JsonIgnoreProperties({"product"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference
    private Product product;

    @Column(name = "reviewer_name", nullable = false)
    private String reviewerName;

    @Column(name = "text", length = 1000, nullable = false)
    private String text;

    @Column(nullable = false)
    private int rating;

    @Column(name = "review_date", nullable = false, updatable = false)
    private LocalDate reviewDate;

    public Review() {
    }

    public Review(Builder builder) {
        this.product      = builder.product;
        this.reviewerName = builder.reviewerName;
        this.text         = builder.text;
        this.rating       = builder.rating;
    }

    public static Builder builder(){
        return new Builder();
    }

    @PrePersist
    protected void onCreate() {
        this.reviewDate = LocalDate.now();
    }


    public Long getReviewId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public String getReviewerName() {
        return reviewerName;
    }
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public static class Builder{
        private Product product;
        private String reviewerName;
        private String text;
        private int rating;

        public Builder product(Product product){
            this.product = product;
            return this;
        }

        public Builder reviewerName(String reviewerName){
            this.reviewerName = reviewerName;
            return this;
        }

        public Builder text(String text){
            this.text = text;
            return this;
        }

        public Builder rating(int rating){
            this.rating = rating;
            return this;
        }

        public Review build(){
            return new Review(this);
        }
    }

}
