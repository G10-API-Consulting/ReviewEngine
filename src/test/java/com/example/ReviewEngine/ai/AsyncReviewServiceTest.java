package com.example.ReviewEngine.ai;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

class AsyncReviewServiceTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private AsyncReviewService async;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateAndSaveReviewsAsync_parsesAndSaves() throws Exception {
        String json = "[{\"review\":\"Nice\",\"writer\":\"Bob\",\"rating\":4}]";
        doNothing().when(reviewService).createReview(anyLong(), any());

        Product p = new Product();
        p.setProductId(1L);

        async.generateAndSaveReviewsAsync(p);

        verify(reviewService, times(1)).createReview(eq(1L), any(ReviewRequest.class));
    }

    @Test
    void generateAndSaveReviewsAsync_logsOnParseError() throws Exception {
        String bad = "not json";
        async.generateAndSaveReviewsAsync(new Product());
    }
}
