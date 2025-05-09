package com.example.ReviewEngine.controller;
import com.example.ReviewEngine.ai.AIClient;
import com.example.ReviewEngine.dto.ProductIdRequest;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private AIClient aiClient;

    @InjectMocks
    private ProductController controller;

    @Captor
    private ArgumentCaptor<ProductRequest> productRequestCaptor;

    private Product dummyProduct;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dummyProduct = new Product();
        dummyProduct.setProductId(42L);
        dummyProduct.setName("TestProduct");
        dummyProduct.setCategory("TestCategory");
        dummyProduct.setTags(Set.of());
    }

    @Test
    void saveProduct_returnsCreatedProduct() {
        ProductRequest req = new ProductRequest();
        req.setName("MyProduct");
        req.setCategory("CategoryX");
        req.setTags(List.of("Tag1", "Tag2"));
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(dummyProduct);

        ResponseEntity<Product> resp = controller.saveProduct(req);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertSame(dummyProduct, resp.getBody());
        verify(productService, times(1)).createProduct(productRequestCaptor.capture());
        ProductRequest captured = productRequestCaptor.getValue();
        assertEquals("MyProduct", captured.getName());
        assertEquals("CategoryX", captured.getCategory());
        assertIterableEquals(List.of("Tag1", "Tag2"), captured.getTags());
    }

    @Test
    void getProductById_returnsProduct() {
        ProductIdRequest req = new ProductIdRequest();
        req.setId(99L);
        when(productService.getProductById(99L)).thenReturn(dummyProduct);

        ResponseEntity<Product> resp = controller.getProduct(req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(dummyProduct, resp.getBody());
        verify(productService, times(1)).getProductById(99L);
    }

    @Test
    void getProducts_triggersAiClientAndReturnsList() {
        List<Product> products = List.of(dummyProduct, dummyProduct);
        when(productService.getAllProducts()).thenReturn(products);

        ResponseEntity<List<Product>> resp = controller.getProducts();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals(2, resp.getBody().size());
        verify(aiClient, times(1)).generateReview();
        verify(productService, times(1)).getAllProducts();
    }
}
