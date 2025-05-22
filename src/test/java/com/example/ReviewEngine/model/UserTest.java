package com.example.ReviewEngine.model;

import com.example.ReviewEngine.controller.UserController;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserById_whenFound_returnsUser() {
        User u = new User();
        u.setId(2L);
        u.setName("Carol");
        when(userRepository.findById(2L)).thenReturn(Optional.of(u));

        ResponseEntity<User> resp = controller.getUserById(2L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody().getName()).isEqualTo("Carol");
    }

    @Test
    void getUserById_whenMissing_returns404() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        ResponseEntity<User> resp = controller.getUserById(5L);

        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
        assertThat(resp.getBody()).isNull();
    }

    @Test
    void getAllUsers_returnsList() {
        User u1 = new User(); u1.setId(1L); u1.setName("Dan");
        User u2 = new User(); u2.setId(2L); u2.setName("Eve");
        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        ResponseEntity<Iterable<User>> resp = controller.getAllUsers();

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).hasSize(2);
    }
}
