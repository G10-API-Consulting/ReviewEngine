package com.example.ReviewEngine.services;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.model.Tag;
import com.example.ReviewEngine.repository.ProductRepository;
import com.example.ReviewEngine.repository.TagRepository;
import com.example.ReviewEngine.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ProductServiceTest {

    @Mock
    private ProductRepository productRepo;

    @Mock
    private TagRepository tagRepo;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProduct_reusesExistingTag() {
        ProductRequest req = new ProductRequest();
        req.setName("P");
        req.setCategory("C");
        req.setTags(List.of("T1"));

        Tag existing = new Tag("T1");
        when(tagRepo.findByName("T1")).thenReturn(Optional.of(existing));
        when(productRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product p = service.createProduct(req);

        assertEquals("P", p.getName());
        assertEquals("C", p.getCategory());
        assertTrue(p.getTags().contains(existing));
        verify(tagRepo, never()).save(argThat(t -> "T1".equals(t.getName()) && t != existing));
        verify(productRepo).save(p);
    }

    @Test
    void createProduct_createsNewTagIfMissing() {
        ProductRequest req = new ProductRequest();
        req.setName("P");
        req.setCategory("C");
        req.setTags(List.of("X"));

        when(tagRepo.findByName("X")).thenReturn(Optional.empty());
        when(tagRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(productRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Product p = service.createProduct(req);

        assertEquals(1, p.getTags().size());
        Tag t = p.getTags().iterator().next();
        assertEquals("X", t.getName());
        verify(tagRepo).save(argThat(nt -> "X".equals(nt.getName())));
    }

    @Test
    void getProductById_notFound_throws() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getProductById(1L));
    }

    @Test
    void getAllProducts_forwardsCall() {
        service.getAllProducts();
        verify(productRepo).findAll();
    }
}

