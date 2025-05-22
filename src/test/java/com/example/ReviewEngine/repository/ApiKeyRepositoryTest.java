package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ApiKeyRepositoryTest {

    @Autowired
    private ApiKeyRepository apiKeyRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByKey_andIsActiveTrue() {
        User user = new User();
        user.setName("T");
        user.setUserName("u");
        user.setPassword("p");
        userRepository.save(user);

        ApiKey key1 = new ApiKey();
        key1.setKey("k1"); key1.setUser(user); key1.setActive(true);
        ApiKey key2 = new ApiKey();
        key2.setKey("k2"); key2.setUser(user); key2.setActive(false);
        apiKeyRepository.save(key1);
        apiKeyRepository.save(key2);

        Optional<ApiKey> found = apiKeyRepository.findByKey("k1").filter(ApiKey::isActive);
        assertThat(found).isPresent();

        List<ApiKey> activeList = apiKeyRepository.findByUserAndIsActiveTrue(user);
        assertThat(activeList).containsExactly(key1);
    }
}
