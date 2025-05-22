package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.LoginRequest;
import com.example.ReviewEngine.dto.RegisterRequest;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import com.example.ReviewEngine.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_success() {
        RegisterRequest req = new RegisterRequest();
        req.setUserName("bob");
        req.setName("Bob");
        req.setPassword("secret");

        when(userRepository.findByUserName("bob")).thenReturn(Optional.empty());

        ResponseEntity<String> resp = controller.registerUser(req);

        assertThat(resp.getStatusCodeValue()).isEqualTo(201);
        assertThat(resp.getBody()).isEqualTo("User registered successfully");
        verify(userRepository).save(argThat(user ->
                "bob".equals(user.getUserName()) &&
                        new BCryptPasswordEncoder().matches("secret", user.getPassword()) &&
                        "Bob".equals(user.getName())
        ));
    }

    @Test
    void registerUser_conflict() {
        RegisterRequest req = new RegisterRequest();
        req.setUserName("bob");
        req.setName("Bob");
        req.setPassword("secret");

        when(userRepository.findByUserName("bob"))
                .thenReturn(Optional.of(new User()));

        ResponseEntity<String> resp = controller.registerUser(req);

        assertThat(resp.getStatusCodeValue()).isEqualTo(409);
        assertThat(resp.getBody()).isEqualTo("Username is already taken");
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginUser_success() {
        String raw = "passw0rd";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode(raw);

        User u = new User();
        u.setUserName("alice");
        u.setPassword(hashed);

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(u));
        when(jwtService.generateToken("alice")).thenReturn("tok123");

        LoginRequest req = new LoginRequest();
        req.setUserName("alice");
        req.setPassword(raw);

        ResponseEntity<String> resp = controller.loginUser(req);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).contains("JWT: tok123");
    }

    @Test
    void loginUser_invalidPassword() {
        String raw = "passw0rd";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode(raw);

        User u = new User();
        u.setUserName("alice");
        u.setPassword(hashed);

        when(userRepository.findByUserName("alice")).thenReturn(Optional.of(u));

        LoginRequest req = new LoginRequest();
        req.setUserName("alice");
        req.setPassword("wrong");

        ResponseEntity<String> resp = controller.loginUser(req);

        assertThat(resp.getStatusCodeValue()).isEqualTo(401);
        assertThat(resp.getBody()).isEqualTo("Invalid credentials");
    }

    @Test
    void loginUser_userNotFound_throws() {
        when(userRepository.findByUserName("nosuch")).thenReturn(Optional.empty());

        LoginRequest req = new LoginRequest();
        req.setUserName("nosuch");
        req.setPassword("x");

        assertThrows(RuntimeException.class, () -> controller.loginUser(req));
    }
}
