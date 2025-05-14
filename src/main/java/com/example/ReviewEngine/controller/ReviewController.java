package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Review> addReview(@PathVariable Long id, @RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.createReview(id, request));
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody ReviewRequest request){
        Review updatedReview = reviewService.updateReview(id , request);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
