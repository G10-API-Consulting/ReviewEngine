package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ProductRepository prodRepo;
    private ReviewRepository revRepo;
    private ReviewService service;

    @BeforeEach
    void setUp() {
        prodRepo = mock(ProductRepository.class);
        revRepo = mock(ReviewRepository.class);
        service = new ReviewService(prodRepo, revRepo);
    }

    @Test
    void createReview_whenProductMissing_throws404() {
        when(prodRepo.findById(1L)).thenReturn(Optional.empty());
        ReviewRequest req = new ReviewRequest.Builder()
                .productId(1L)
                .reviewerName("X")
                .text("Nice!")
                .rating(4)
                .build();
        assertThatThrownBy(() -> service.createReview(req))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404");
    }

    @Test
    void createReview_savesReview() {
        Product p = new Product();
        when(prodRepo.findById(2L)).thenReturn(Optional.of(p));
        when(revRepo.save(any(Review.class))).thenAnswer(i -> i.getArgument(0));

        ReviewRequest req = new ReviewRequest.Builder()
                .productId(2L)
                .reviewerName("Y")
                .text("Great")
                .rating(5)
                .build();
        Review r = service.createReview(req);

        assertThat(r.getReviewerName()).isEqualTo("Y");
        assertThat(r.getText()).isEqualTo("Great");
        assertThat(r.getRating()).isEqualTo(5);
        verify(revRepo).save(r);
    }
}
