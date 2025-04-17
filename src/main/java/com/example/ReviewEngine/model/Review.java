package com.example.ReviewEngine.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
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

    public Review(Product product,
                  String reviewerName,
                  String text,
                  int rating) {
        this.product      = product;
        this.reviewerName = reviewerName;
        this.text         = text;
        this.rating       = rating;
    }

    @PrePersist
    protected void onCreate() {
        this.reviewDate = LocalDate.now();
    }


    public Long getReviewId() {
        return reviewId;
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
}
