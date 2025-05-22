package com.example.ReviewEngine.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ReviewRequest {
    @NotBlank(message = "Author for review required")
    private String reviewerName;
    @NotBlank(message = "Review text required")
    private String text;
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private int rating;

    public ReviewRequest() {
    }

    private ReviewRequest(Builder builder) {
        this.reviewerName = builder.reviewerName;
        this.text = builder.text;
        this.rating = builder.rating;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String reviewerName;
        private String text;
        private int rating;


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