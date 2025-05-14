package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.exception.ProductNotFoundException;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ProductRepository productRepository, ReviewRepository reviewRepository){
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review createReview(Long id, ReviewRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND + " Product not found"));

        Review review = Review.builder()
                .product(product)
                .reviewerName(request.getReviewerName())
                .text(request.getText())
                .rating(request.getRating())
                .build();

        return reviewRepository.save(review);
    }

    public Review updateReview(Long id, ReviewRequest request){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND + " Product not found"));

        review.setReviewerName(request.getReviewerName());
        review.setText(request.getText());
        review.setRating(request.getRating());
        review.setReviewDate();

        return reviewRepository.save(review);
    }

    public void deleteReview(Long id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(HttpStatus.NOT_FOUND +  " Review not found"));
        reviewRepository.delete(review);
    }
}
