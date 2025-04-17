package com.example.ReviewEngine.model;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String writtenBy;

    @Temporal(TemporalType.DATE)
    private Date date;

    private int rating;

    @Column(length = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review() {}

    public Review(String writtenBy, Date date, int rating, String text, Product product) {
        this.writtenBy = writtenBy;
        this.date = date;
        this.rating = rating;
        this.text = text;
        this.product = product;
    }
    public Long getReviewId() {
        return reviewId;
    }
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    public String getWrittenBy() {
        return writtenBy;
    }
    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getText() {
        return text;

    }
    public void setText(String text) {
        this.text = text;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
        

}
