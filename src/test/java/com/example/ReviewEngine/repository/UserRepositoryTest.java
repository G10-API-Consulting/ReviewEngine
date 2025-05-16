package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;

    @Test
    void findByUserName_andUniqueConstraint() {
        User u = new User();
        u.setName("Bob");
        u.setUserName("bob");
        u.setPassword("pwd");
        userRepository.save(u);

        Optional<User> fetched = userRepository.findByUserName("bob");
        assertThat(fetched).isPresent()
                .get()
                .extracting(User::getUserName)
                .isEqualTo("bob");
    }
}
