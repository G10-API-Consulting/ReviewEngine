package com.example.ReviewEngine.controller;
import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    @InjectMocks
    private ReviewController controller;

    @Mock
    private ReviewService reviewService;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void addReview_returnsReview() throws Exception {
        Review saved = Review.builder()
                .reviewerName("Alice")
                .text("Great!")
                .rating(5)
                .build();
        saved.setReviewDate();
        when(reviewService.createReview(eq(123L), any())).thenReturn(saved);

        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("Alice")
                .text("Great!")
                .rating(5)
                .build();

        mvc.perform(post("/api/product/123/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerName").value("Alice"))
                .andExpect(jsonPath("$.text").value("Great!"))
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void updateReview_returnsUpdated() throws Exception {
        Review updated = Review.builder()
                .reviewerName("Bob")
                .text("Okay")
                .rating(3)
                .build();
        updated.setReviewDate();
        when(reviewService.updateReview(eq(55L), any())).thenReturn(updated);

        ReviewRequest req = ReviewRequest.builder()
                .reviewerName("Bob")
                .text("Okay")
                .rating(3)
                .build();

        mvc.perform(put("/api/product/review/55")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerName").value("Bob"))
                .andExpect(jsonPath("$.text").value("Okay"))
                .andExpect(jsonPath("$.rating").value(3));
    }

    @Test
    void deleteReview_returnsNoContent() throws Exception {
        doNothing().when(reviewService).deleteReview(77L);

        mvc.perform(delete("/api/product/review/77"))
                .andExpect(status().isNoContent());

        verify(reviewService).deleteReview(77L);
    }

    @Test
    void getReviewById_returnsReview() throws Exception {
        Review r = Review.builder()
                .reviewerName("Carol")
                .text("Fine")
                .rating(4)
                .build();
        r.setReviewDate();
        when(reviewService.getReviewById(88L)).thenReturn(r);

        mvc.perform(get("/api/product/review/88"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewerName").value("Carol"))
                .andExpect(jsonPath("$.text").value("Fine"))
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void getAllReviews_returnsList() throws Exception {
        Review r1 = Review.builder()
                .reviewerName("Dave")
                .text("Bad")
                .rating(1)
                .build();
        r1.setReviewDate();
        when(reviewService.getAllReview()).thenReturn(List.of(r1));
        mvc.perform(get("/api/product/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewerName").value("Dave"))
                .andExpect(jsonPath("$[0].text").value("Bad"))
                .andExpect(jsonPath("$[0].rating").value(1));
    }
}
