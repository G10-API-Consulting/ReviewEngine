package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReviewControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ReviewService mockReviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(reviewController)
                .build();
    }

    @Test
    void whenPostValidReview_thenReturnsSameReview() throws Exception {
        // prepare payload
        var reviewRequest = ReviewRequest.builder()
                .productId(10L)
                .reviewerName("Alice")
                .text("Very good")
                .rating(4)
                .build();

        // mock service
        var expected = Review.builder()
                .product(null)
                .reviewerName("Alice")
                .text("Very good")
                .rating(4)
                .build();
        given(mockReviewService.createReview(any(ReviewRequest.class)))
                .willReturn(expected);

        // execute & verify
        mockMvc.perform(post("/api/review/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reviewerName").value("Alice"))
                .andExpect(jsonPath("$.text").value("Very good"))
                .andExpect(jsonPath("$.rating").value(4));
    }
}
