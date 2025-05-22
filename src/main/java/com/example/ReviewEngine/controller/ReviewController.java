package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Review> addReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.createReview(id, request));
    }

    @PutMapping("/review/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request){
        Review updatedReview = reviewService.updateReview(id , request);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/review/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id){
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<Review>> getReview(){
        return ResponseEntity.ok(reviewService.getAllReview());
    }

}
