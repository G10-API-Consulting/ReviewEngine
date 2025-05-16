package com.example.ReviewEngine.service;

import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.exception.ProductNotFoundException;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock ProductRepository productRepository;
    @Mock TagRepository tagRepository;
    @InjectMocks ProductService productService;

    @Test
    void createProduct_savesNewTagsAndProduct() {
        // prepare request
        ProductRequest req = new ProductRequest();
        req.setName("P");
        req.setCategory("C");
        req.setTags(List.of("t1", "t2"));

        // stub tag lookups & saves
        when(tagRepository.findByName("t1"))
                .thenReturn(Optional.empty());
        Tag newTag1 = new Tag("t1");
        when(tagRepository.save(argThat(tag -> "t1".equals(tag.getName()))))
                .thenReturn(newTag1);

        when(tagRepository.findByName("t2"))
                .thenReturn(Optional.empty());
        Tag newTag2 = new Tag("t2");
        when(tagRepository.save(argThat(tag -> "t2".equals(tag.getName()))))
                .thenReturn(newTag2);

        // stub final product save
        Product saved = Product.builder()
                .name("P")
                .category("C")
                .tags(Set.of(newTag1, newTag2))
                .build();
        when(productRepository.save(any()))
                .thenReturn(saved);

        // call
        Product result = productService.createProduct(req);

        // verify
        assertThat(result.getTags())
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("t1", "t2");
        verify(productRepository).save(any());
    }

    @Test
    void getProductById_found() {
        Product p = new Product();
        // we don't need to set an ID on p — we're going to compare the same instance
        when(productRepository.findById(5L))
                .thenReturn(Optional.of(p));

        Product result = productService.getProductById(5L);
        assertThat(result).isSameAs(p);
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProductById(99L))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void getAllProducts_returnsList() {
        Product a = new Product();
        Product b = new Product();
        when(productRepository.findAll())
                .thenReturn(List.of(a, b));

        List<Product> all = productService.getAllProducts();
        assertThat(all).containsExactly(a, b);
    }

    @Test
    void deleteProduct_success() {
        Product toDelete = new Product();
        when(productRepository.findById(7L))
                .thenReturn(Optional.of(toDelete));

        productService.deleteProduct(7L);
        verify(productRepository).delete(toDelete);
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.findById(42L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.deleteProduct(42L))
                .isInstanceOf(ProductNotFoundException.class);
    }
}
