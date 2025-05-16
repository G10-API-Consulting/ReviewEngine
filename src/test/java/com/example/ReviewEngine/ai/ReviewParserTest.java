package com.example.ReviewEngine.ai;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ReviewParserTest {

    @Test
    void parse_extractsCorrectFields() {
        String input =
                "REVIEW: \"Super!\"\n" +
                        "WRITER: \"Charlie\"\n" +
                        "RATING: 3\n";

        Product p = new Product();
        ReviewParser parser = new ReviewParser();
        Review r = parser.parse(input, p);

        assertThat(r.getText()).isEqualTo("Super!");
        assertThat(r.getReviewerName()).isEqualTo("Charlie");
        assertThat(r.getRating()).isEqualTo(3);
        assertThat(r.getProduct()).isSameAs(p);
    }

    @Test
    void parse_handlesMissingFields() {
        String input = "WRITER: \"Dana\"\n";
        ReviewParser parser = new ReviewParser();
        Review r = parser.parse(input, new Product());

        assertThat(r.getText()).isNull();
        assertThat(r.getReviewerName()).isEqualTo("Dana");
        assertThat(r.getRating()).isEqualTo(5);
    }
}
