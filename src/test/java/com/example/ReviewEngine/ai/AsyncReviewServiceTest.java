package com.example.ReviewEngine.ai;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AsyncReviewServiceTest {

    @Mock
    private AIClient aiClient;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private AsyncReviewService asyncReviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateAndSaveReviewsAsync_parsesAndSaves() {
        Product product = new Product();
        product.setProductId(42L);
        product.setName("X");
        product.setCategory("C");
        product.setTags(Set.of());

        String fakeJson = "[{\"review\":\"Good\",\"writer\":\"Alice\",\"rating\":5}]";
        when(aiClient.generateReview(product)).thenReturn(fakeJson);

        Review fakeReview = new Review.Builder()
                .reviewerName("Alice")
                .text("Good")
                .rating(5)
                .build();
        when(reviewService.createReview(eq(42L), any())).thenReturn(fakeReview);

        CompletableFuture<Void> fut = asyncReviewService.generateAndSaveReviewsAsync(product);
        fut.join();
        verify(reviewService, times(1)).createReview(eq(42L), any());
    }
}
