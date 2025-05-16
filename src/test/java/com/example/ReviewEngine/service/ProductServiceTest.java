package com.example.ReviewEngine.service;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_savesNewTagsAndProduct() {
        ProductRequest req = new ProductRequest();
        req.setName("Prod");
        req.setCategory("Cat");
        req.setTags(List.of("t1", "t2"));

        Tag existing = new Tag("t1");
        when(tagRepository.findByName("t1")).thenReturn(Optional.of(existing));
        when(tagRepository.findByName("t2")).thenReturn(Optional.empty());

        when(tagRepository.save(any(Tag.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, Tag.class));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0, Product.class);
                    p.setProductId(123L);
                    return p;
                });

        Product saved = productService.createProduct(req);
        assertThat(saved.getProductId()).isEqualTo(123L);
        assertThat(saved.getTags()).extracting(Tag::getName)
                .containsExactlyInAnyOrder("t1", "t2");

        verify(tagRepository).save(argThat(t -> "t2".equals(t.getName())));
        verify(productRepository).save(any());
    }
}
