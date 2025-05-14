package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReviewService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(ProductRepository productRepository, ReviewRepository reviewRepository){
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Review createReview(ReviewRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        Review review = Review.builder()
                .product(product)
                .reviewerName(request.getReviewerName())
                .text(request.getText())
                .rating(request.getRating())
                .build();

        return reviewRepository.save(review);
    }
}
