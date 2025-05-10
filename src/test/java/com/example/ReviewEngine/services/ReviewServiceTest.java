package com.example.ReviewEngine.services;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.ReviewRepository;
import com.example.ReviewEngine.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private ReviewRequest request;

    @BeforeEach
    void setUp() {
        request = new ReviewRequest();
        ReflectionTestUtils.setField(request, "productId", 42L);
        ReflectionTestUtils.setField(request, "reviewerName", "Alice");
        ReflectionTestUtils.setField(request, "text", "Great product!");
        ReflectionTestUtils.setField(request, "rating", 5);
    }

    @Test
    void createReview_success() {
        Product prod = new Product();
        ReflectionTestUtils.setField(prod, "productId", 42L);
        when(productRepository.findById(42L)).thenReturn(Optional.of(prod));

        Review saved = Review.builder()
                .product(prod)
                .reviewerName("Alice")
                .text("Great product!")
                .rating(5)
                .build();
        ReflectionTestUtils.setField(saved, "id", 100L);
        ReflectionTestUtils.setField(saved, "reviewDate", LocalDate.of(2025, 5, 9));
        when(reviewRepository.save(any(Review.class))).thenReturn(saved);

        Review result = reviewService.createReview(request);

        assertThat(result.getReviewId()).isEqualTo(100L);
        assertThat(result.getProduct()).isSameAs(prod);
        assertThat(result.getReviewerName()).isEqualTo("Alice");
        assertThat(result.getText()).isEqualTo("Great product!");
        assertThat(result.getRating()).isEqualTo(5);
        assertThat(result.getReviewDate()).isEqualTo(LocalDate.of(2025, 5, 9));

        verify(productRepository).findById(42L);
        verify(reviewRepository).save(any());
    }

    @Test
    void createReview_productNotFound_throws404() {
        when(productRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.createReview(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404 NOT_FOUND");

        verify(productRepository).findById(42L);
        verifyNoInteractions(reviewRepository);
    }
}
