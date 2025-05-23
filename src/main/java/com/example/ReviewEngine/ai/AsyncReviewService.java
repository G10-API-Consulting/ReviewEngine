package com.example.ReviewEngine.ai;

import com.example.ReviewEngine.dto.ReviewJson;
import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ReviewService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncReviewService {


    private final AIClient aiClient;
    private final ReviewService reviewService;

    public AsyncReviewService(AIClient aiClient, ReviewService reviewService) {
        this.aiClient = aiClient;
        this.reviewService = reviewService;
    }

    @Async
    public CompletableFuture<Void> generateAndSaveReviewsAsync(Product product, List<String> des) {
        try {
            String jsonResponse = aiClient.generateReview(product, des);
            System.out.println("JSON från AI:\n" + jsonResponse);
            ObjectMapper mapper = new ObjectMapper();
            List<ReviewJson> reviews = mapper.readValue(jsonResponse, new TypeReference<>() {
            });
            if (reviews != null) {
                for (ReviewJson r : reviews) {
                    ReviewRequest request = ReviewRequest.builder()
                            .reviewerName(r.getWriter())
                            .text(r.getReview())
                            .rating(r.getRating())
                            .build();

                    reviewService.createReview(product.getProductId(), request);
                }
            }
        } catch (Exception e) {
            System.err.println("Fel vid asynkron review-generering: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
}
