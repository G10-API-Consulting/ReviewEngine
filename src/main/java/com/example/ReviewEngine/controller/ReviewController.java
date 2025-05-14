package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @PostMapping("/save")
    public ResponseEntity<Review> addReview(@RequestBody ReviewRequest request){
        return ResponseEntity.ok(reviewService.createReview(request));
    }
}
