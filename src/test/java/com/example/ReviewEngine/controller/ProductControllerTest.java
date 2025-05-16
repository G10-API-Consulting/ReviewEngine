package com.example.ReviewEngine.controller;
import com.example.ReviewEngine.dto.ProductRequest;
import com.example.ReviewEngine.model.Product;
import com.example.ReviewEngine.service.ProductService;
import com.example.ReviewEngine.ai.AsyncReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    @InjectMocks
    private ProductController controller;

    @Mock
    private ProductService productService;

    @Mock
    private AsyncReviewService asyncReviewService;

    private MockMvc mvc;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getProducts_returnsList() throws Exception {
        Product p = new Product();
        p.setProductId(1L);
        p.setName("Alpha");
        p.setCategory("Cat");
        when(productService.getAllProducts()).thenReturn(List.of(p));

        mvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].name").value("Alpha"));
    }

    @Test
    void saveProduct_returnsCreated() throws Exception {
        ProductRequest req = new ProductRequest();
        req.setName("New");
        req.setCategory("C");
        req.setTags(List.of());

        Product saved = new Product();
        saved.setProductId(2L);
        saved.setName("New");
        saved.setCategory("C");

        when(productService.createProduct(any())).thenReturn(saved);
        when(asyncReviewService.generateAndSaveReviewsAsync(saved))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(productService.getProductById(2L)).thenReturn(saved);

        mvc.perform(post("/product/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productId").value(2))
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void getProduct_returnsOne() throws Exception {
        Product p = new Product();
        p.setProductId(3L);
        p.setName("X");
        p.setCategory("Y");
        when(productService.getProductById(3L)).thenReturn(p);

        mvc.perform(get("/product/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Y"));
    }

    @Test
    void deleteProduct_returnsNoContent() throws Exception {
        doNothing().when(productService).deleteProduct(4L);

        mvc.perform(delete("/product/4"))
                .andExpect(status().isNoContent());
    }
}
