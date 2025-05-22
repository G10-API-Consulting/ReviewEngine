package com.example.ReviewEngine.repository;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class ReviewRepositoryTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private ProductRepository productRepository;

    @Test
    void save_andFindById() {
        Product p = new Product();
        p.setName("Prod"); p.setCategory("Cat");
        p.setCustomerId(1L);
        productRepository.save(p);

        Review r = Review.builder()
                .product(p)
                .reviewerName("Alice")
                .text("Nice")
                .rating(4)
                .build();

        reviewRepository.save(r);

        Optional<Review> fetched = reviewRepository.findById(r.getReviewId());
        assertThat(fetched).isPresent()
                .get()
                .extracting(Review::getReviewerName, Review::getRating)
                .containsExactly("Alice",4);
    }
}
