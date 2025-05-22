package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

class ReviewTest {

    @Test
    void builder_setsAllFieldsCorrectly() {
        Product p = new Product();
        Review r = Review.builder()
                .product(p)
                .reviewerName("Alice")
                .text("Excellent")
                .rating(5)
                .build();

        assertThat(r.getProduct()).isSameAs(p);
        assertThat(r.getReviewerName()).isEqualTo("Alice");
        assertThat(r.getText()).isEqualTo("Excellent");
        assertThat(r.getRating()).isEqualTo(5);
    }

    @Test
    void mutableFields_canBeModifiedViaSetters() {
        Review r = new Review();
        Product p = new Product();
        r.setProduct(p);
        r.setReviewerName("Bob");
        r.setText("Good");
        r.setRating(3);

        assertThat(r.getProduct()).isSameAs(p);
        assertThat(r.getReviewerName()).isEqualTo("Bob");
        assertThat(r.getText()).isEqualTo("Good");
        assertThat(r.getRating()).isEqualTo(3);
    }

    @Test
    void onCreate_initializesReviewDate() throws Exception {
        Review r = new Review();
        assertThat(r.getReviewDate()).isNull();

        Method onCreate = Review.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(r);

        LocalDate d = r.getReviewDate();
        assertThat(d).isNotNull();
        assertThat(d).isInstanceOf(LocalDate.class);
    }

    @Test
    void setReviewDate_updatesToNow() {
        Review r = new Review();
        r.setReviewDate();
        LocalDate today = LocalDate.now();
        assertThat(r.getReviewDate()).isEqualTo(today);
    }
}
