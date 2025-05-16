package com.example.ReviewEngine.service;
import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.exception.ProductNotFoundException;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewService reviewService;

    @Test
    void createReview_saves() {
        Product p = new Product();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(p));
        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("A")
                .text("X")
                .rating(1)
                .build();

        Review saved = Review.builder()
                .product(p)
                .reviewerName("A")
                .text("X")
                .rating(1)
                .build();
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(saved);
        Review result = reviewService.createReview(1L, req);

        assertThat(result.getReviewerName()).isEqualTo("A");
        assertThat(result.getText()).isEqualTo("X");
        assertThat(result.getRating()).isEqualTo(1);
        verify(reviewRepository).save(any());
    }

    @Test
    void createReview_productNotFound_throws() {
        when(productRepository.findById(2L))
                .thenReturn(Optional.empty());

        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("B")
                .text("Y")
                .rating(2)
                .build();

        assertThatThrownBy(() -> reviewService.createReview(2L, req))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void updateReview_modifiesAndSaves() {
        Review existing = Review.builder()
                .product(new Product())
                .reviewerName("old")
                .text("old")
                .rating(1)
                .build();
        when(reviewRepository.findById(5L))
                .thenReturn(Optional.of(existing));

        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("newName")
                .text("newText")
                .rating(3)
                .build();

        Review updated = Review.builder()
                .product(existing.getProduct())
                .reviewerName("newName")
                .text("newText")
                .rating(3)
                .build();
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(updated);

        Review result = reviewService.updateReview(5L, req);

        assertThat(result.getReviewerName()).isEqualTo("newName");
        assertThat(result.getText()).isEqualTo("newText");
        assertThat(result.getRating()).isEqualTo(3);
        verify(reviewRepository).save(existing);
    }

    @Test
    void updateReview_notFound_throws() {
        when(reviewRepository.findById(100L))
                .thenReturn(Optional.empty());

        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("X")
                .text("Y")
                .rating(5)
                .build();

        assertThatThrownBy(() -> reviewService.updateReview(100L, req))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void deleteReview_deletes() {
        Review r = Review.builder()
                .product(new Product())
                .reviewerName("Z")
                .text("Z")
                .rating(2)
                .build();
        when(reviewRepository.findById(8L)).thenReturn(Optional.of(r));
        reviewService.deleteReview(8L);
        verify(reviewRepository).delete(r);
    }

    @Test
    void deleteReview_notFound_throws() {
        when(reviewRepository.findById(9L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.deleteReview(9L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void getAllReview_returnsList() {
        Review r1 = Review.builder()
                .product(new Product())
                .reviewerName("B")
                .text("Y")
                .rating(2)
                .build();
        when(reviewRepository.findAll()).thenReturn(List.of(r1));

        List<Review> all = reviewService.getAllReview();
        assertThat(all).containsExactly(r1);
    }

    @Test
    void getReviewById_found() {
        Review r2 = Review.builder()
                .product(new Product())
                .reviewerName("C")
                .text("Z")
                .rating(4)
                .build();
        when(reviewRepository.findById(7L))
                .thenReturn(Optional.of(r2));

        Review result = reviewService.getReviewById(7L);
        assertThat(result).isSameAs(r2);
    }

    @Test
    void getReviewById_notFound_throws() {
        when(reviewRepository.findById(11L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewById(11L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
