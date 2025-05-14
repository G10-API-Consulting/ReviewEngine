package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class ReviewTest {

    @Test
    void builder_andGetters(){
        Product product = new Product();
        product.setName("A");

        Review review = Review.builder()
                .product(product)
                .reviewerName("Ana")
                .text("My review")
                .rating(3)
                .build();

        assertSame(product, review.getProduct());
        assertEquals("Ana", review.getReviewerName());
        assertEquals("My review", review.getText());
        assertEquals(3, review.getRating());
        assertNull(review.getReviewDate());
        review.onCreate();
        assertNotNull(review.getReviewDate());
        assertTrue(review.getReviewDate().isEqual(LocalDate.now()));
    }


}