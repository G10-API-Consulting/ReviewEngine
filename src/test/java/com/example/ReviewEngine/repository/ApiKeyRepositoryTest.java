package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ApiKeyRepositoryTest {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private User user;

    @BeforeEach
    void initUser() {
        user = new User();
        user.setName("Carol");
        user.setUserName("carol");
        user.setPassword("pw");
        user.setRole(User.Role.CUSTOMER);
        userRepository.save(user);
    }

    @Test
    void shouldHandleActiveAndInactiveKeysCorrectly() {
        ApiKey activeKey = new ApiKey();
        activeKey.setKey("active-key");
        activeKey.setUser(user);
        activeKey.setActive(true);

        ApiKey oldKey = new ApiKey();
        oldKey.setKey("old-key");
        oldKey.setUser(user);
        oldKey.setActive(false);

        apiKeyRepository.save(activeKey);
        apiKeyRepository.save(oldKey);
        em.flush();
        em.clear();

        Optional<ApiKey> foundActive = apiKeyRepository.findByKey("active-key");
        assertThat(foundActive).isPresent();
        assertThat(foundActive.get().isActive()).isTrue();

        Optional<ApiKey> foundOld = apiKeyRepository.findByKey("old-key");
        assertThat(foundOld).isPresent();
        assertThat(foundOld.get().isActive()).isFalse();

        List<ApiKey> activeList = apiKeyRepository.findByUserAndIsActiveTrue(user);
        assertThat(activeList)
                .hasSize(1)
                .extracting(ApiKey::getKey)
                .containsExactly("active-key");
    }
}
