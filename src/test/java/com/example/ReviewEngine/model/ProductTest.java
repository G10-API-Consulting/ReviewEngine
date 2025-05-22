package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Set;
import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    void builder_setsAllFieldsCorrectly() {
        Tag t1 = new Tag("foo");
        Tag t2 = new Tag("bar");
        Review r1 = Review.builder()
                .product(null)
                .reviewerName("Alice")
                .text("Nice")
                .rating(4)
                .build();
        Product p = Product.builder()
                .name("MyProduct")
                .category("Gadgets")
                .tags(Set.of(t1, t2))
                .reviews(List.of(r1))
                .build();

        assertThat(p.getName()).isEqualTo("MyProduct");
        assertThat(p.getCategory()).isEqualTo("Gadgets");
        assertThat(p.getTags()).containsExactlyInAnyOrder(t1, t2);
        assertThat(p.getReviews()).containsExactly(r1);
    }

    @Test
    void mutableFields_canBeModifiedViaSetters() {
        Product p = new Product();
        p.setName("X");
        p.setCategory("Y");
        Tag t = new Tag("tag");
        p.setTags(Set.of(t));
        Review r = Review.builder()
                .product(null)
                .reviewerName("Bob")
                .text("Test")
                .rating(2)
                .build();
        p.setReviews(List.of(r));

        assertThat(p.getName()).isEqualTo("X");
        assertThat(p.getCategory()).isEqualTo("Y");
        assertThat(p.getTags()).containsExactly(t);
        assertThat(p.getReviews()).containsExactly(r);
    }

    @Test
    void onCreate_initializesCreatedDate() throws Exception {
        Product p = new Product();
        assertThat(p.getCreatedDate()).isNull();
        Method onCreate = Product.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(p);

        Date cd = p.getCreatedDate();
        assertThat(cd).isNotNull();
        assertThat(cd).isInstanceOf(Date.class);
    }
}
