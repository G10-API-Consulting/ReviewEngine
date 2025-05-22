package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.Product;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    private Product prodA;
    private Product prodB;
    private Product prodC;

    @BeforeEach
    void setUp() {
        prodA = new Product();
        prodA.setName("Alpha");
        prodA.setCategory("Cat");
        prodA.setCustomerId(1L);
        em.persist(prodA);

        prodB = new Product();
        prodB.setName("Beta");
        prodB.setCategory("Cat");
        prodB.setCustomerId(1L);
        em.persist(prodB);

        prodC = new Product();
        prodC.setName("Gamma");
        prodC.setCategory("Cat");
        prodC.setCustomerId(2L);
        em.persist(prodC);

        em.flush();
    }

    @Test
    void findByCustomerIdOrderByNameAsc_returnsOnlyThatCustomersProducts_sortedByName() {
        List<Product> c1 = productRepository.findByCustomerIdOrderByNameAsc(1L);
        assertThat(c1).extracting(Product::getName)
                .containsExactly("Alpha", "Beta");
    }

    @Test
    void findByCustomerId_withSort_allowsCustomSorting() {
        List<Product> c1Desc = productRepository.findByCustomerId(
                1L, Sort.by(Sort.Direction.DESC, "name")
        );
        assertThat(c1Desc).extracting(Product::getName)
                .containsExactly("Beta", "Alpha");

        List<Product> c2ByDate = productRepository.findByCustomerId(
                2L, Sort.by(Sort.Direction.ASC, "createdDate")
        );
        assertThat(c2ByDate).hasSize(1)
                .first()
                .extracting(Product::getName)
                .isEqualTo("Gamma");
    }

    @Test
    void findByCustomerIdOrderByNameAsc_noResults_forOtherCustomer() {
        List<Product> none = productRepository.findByCustomerIdOrderByNameAsc(3L);
        assertThat(none).isEmpty();
    }
}
