package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
