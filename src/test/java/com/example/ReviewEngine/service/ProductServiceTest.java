package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepo;
    private TagRepository tagRepo;
    private ProductService service;

    @BeforeEach
    void setUp() {
        productRepo = mock(ProductRepository.class);
        tagRepo = mock(TagRepository.class);
        service = new ProductService(productRepo, tagRepo);
    }

    @Test
    void createProduct_createsNewTagsIfNeeded() {
        ProductRequest req = new ProductRequest();
        req.setName("Gadget");
        req.setCategory("Electronics");
        req.setTags(List.of("Smart", "New"));

        when(tagRepo.findByName("Smart")).thenReturn(Optional.empty());
        when(tagRepo.findByName("New")).thenReturn(Optional.empty());
        when(tagRepo.save(any(Tag.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var product = service.createProduct(req);
        Set<Tag> tags = product.getTags();

        assertThat(tags).hasSize(2)
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Smart", "New");

        verify(tagRepo, times(2)).save(any(Tag.class));
        verify(productRepo).save(product);
    }
}
