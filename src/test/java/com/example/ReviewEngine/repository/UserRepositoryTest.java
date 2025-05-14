package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUserName_whenUserExists() {
        User user = new User();
        user.setName("Bob");
        user.setUserName("bob123");
        user.setPassword("secret");
        user.setRole(User.Role.CUSTOMER);
        userRepository.save(user);
        Optional<User> found = userRepository.findByUserName("bob123");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Bob");
        assertThat(found.get().getUserName()).isEqualTo("bob123");
    }

    @Test
    void shouldReturnEmpty_whenUserNameNotFound() {
        Optional<User> found = userRepository.findByUserName("doesnotexist");
        assertThat(found).isEmpty();
    }
}
