package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;


class ProductTest {
    @Test
    void builder_setsAllFields(){
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        Set<Tag> tags = new HashSet<>();
        tags.add(tag1);
        tags.add(tag2);
        Product product = Product.builder()
                .name("TestProduct")
                .category("TestCategory")
                .tags(tags)
                .build();

        assertEquals("TestProduct", product.getName());
        assertEquals("TestCategory", product.getCategory());
        assertSame(tags, product.getTags());
        assertNotNull(product.getReviews());
        assertTrue(product.getReviews().isEmpty());
    }

    @Test
    void prePersist_assignsCreateDate(){
        Product product = new Product();
        assertNull(product.getCreatedDate());
        product.onCreate();    // @PrePersist-metoder anropas vanligtvis av JPA
        assertNotNull(product.getCreatedDate());
        assertTrue(product.getCreatedDate() instanceof Date);
    }
}