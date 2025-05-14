package com.example.ReviewEngine.ai;

import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ReviewParserTest {

    private ReviewParser parser;
    private Product dummyProduct;

    @BeforeEach
    void setUp() {
        parser = new ReviewParser();
        dummyProduct = new Product();
        dummyProduct.setProductId(1L);
    }

    @Test
    void parse_validResponse_returnsReview() {
        String response = """
            REVIEW: "Fantastisk produkt som överträffade alla förväntningar."
            WRITER: "Anna Svensson"
            RATING: 5
            """;

        Review review = parser.parse(response, dummyProduct);

        assertThat(review).isNotNull();
        assertThat(review.getProduct()).isEqualTo(dummyProduct);
        assertThat(review.getText())
                .isEqualTo("Fantastisk produkt som överträffade alla förväntningar.");
        assertThat(review.getReviewerName()).isEqualTo("Anna Svensson");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @Test
    void parse_missingRating_usesDefault() {
        String response = """
            REVIEW: "Bra men inte perfekt."
            WRITER: "Erik"
            """;

        Review review = parser.parse(response, dummyProduct);
        assertThat(review.getText()).isEqualTo("Bra men inte perfekt.");
        assertThat(review.getReviewerName()).isEqualTo("Erik");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @Test
    void parse_nonNumericRating_usesDefault() {
        String response = """
            REVIEW: "Oklart betyg."
            WRITER: "Test"
            RATING: fyra
            """;

        Review review = parser.parse(response, dummyProduct);

        assertThat(review.getText()).isEqualTo("Oklart betyg.");
        assertThat(review.getReviewerName()).isEqualTo("Test");
        assertThat(review.getRating()).isEqualTo(5);
    }

    @Test
    void parse_extraLines_ignoresThem() {
        String response = """
            REVIEW: "Toppen!"
            WRITER: "Karin"
            RATING: 4
            EXTRA: This line should be ignored
            """;

        Review review = parser.parse(response, dummyProduct);

        assertThat(review.getReviewerName()).isEqualTo("Karin");
        assertThat(review.getRating()).isEqualTo(4);
        assertThat(review.getText()).isEqualTo("Toppen!");
    }
}
