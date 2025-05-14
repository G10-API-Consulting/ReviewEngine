package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.ai.AIClient;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.dto.ReviewRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import com.example.ReviewEngine.service.ProductService;
import com.example.ReviewEngine.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Mock
    private ProductService prodService;

    @Mock
    private ReviewService revService;

    @Mock
    private AIClient aiClient;

    private ProductController controller;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Create controller instance with mocked dependencies
        controller = new ProductController(prodService, aiClient, revService);

        // Build standalone MockMvc (with Jackson support)
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();

        // Shared fixture
        savedProduct = new Product();
        savedProduct.setProductId(99L);
        savedProduct.setName("Test");
        savedProduct.setCategory("Cat");
    }

    @Test
    void saveProduct_createsAndReturnsUpdated() throws Exception {
        // 1) Prepare request DTO
        ProductRequest req = new ProductRequest();
        req.setName("Test");
        req.setCategory("Cat");
        req.setTags(List.of());

        // 2) Prepare AI-generated review
        Review generatedReview = Review.builder()
                .product(savedProduct)
                .reviewerName("R")
                .text("T")
                .rating(5)
                .build();

        // 3) Stub service & AI calls
        given(prodService.createProduct(any(ProductRequest.class)))
                .willReturn(savedProduct);

        given(aiClient.generateReview(savedProduct))
                .willReturn(generatedReview);

        given(revService.createReview(any(ReviewRequest.class)))
                .willReturn(generatedReview);

        given(prodService.getProductById(99L))
                .willReturn(savedProduct);

        // --- act & assert ---
        mvc.perform(post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(99))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.category").value("Cat"));
    }
}
