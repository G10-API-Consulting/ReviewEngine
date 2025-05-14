package com.example.ReviewEngine.dto;

public class ReviewRequest {
    private Long productId;
    private String reviewerName;
    private String text;
    private int rating;

    public ReviewRequest() {
    }

    private ReviewRequest(Builder builder) {
        this.productId = builder.productId;
        this.reviewerName = builder.reviewerName;
        this.text = builder.text;
        this.rating = builder.rating;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long productId;
        private String reviewerName;
        private String text;
        private int rating;

        public Builder productId(Long productId) {
            this.productId = productId;
            return this;
        }

        public Builder reviewerName(String reviewerName) {
            this.reviewerName = reviewerName;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder rating(int rating) {
            this.rating = rating;
            return this;
        }

        public ReviewRequest build() {
            return new ReviewRequest(this);
        }
    }

    public Long getProductId() {
        return productId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getText() {
        return text;
    }

    public int getRating() {
        return rating;
    }
}