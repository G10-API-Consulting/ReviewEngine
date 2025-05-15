package com.example.ReviewEngine.controller;
import com.example.ReviewEngine.dto.LoginRequest;
import com.example.ReviewEngine.dto.RegisterRequest;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import com.example.ReviewEngine.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController controller;

    private RegisterRequest regReq;
    private LoginRequest loginReq;
    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        regReq = new RegisterRequest();
        regReq.setName("Test");
        regReq.setUserName("user1");
        regReq.setPassword("pass");

        loginReq = new LoginRequest();
        loginReq.setUserName("user1");
        loginReq.setPassword("pass");

        user = new User();
        user.setUserName("user1");
        user.setName("Test");
        user.setPassword(new BCryptPasswordEncoder().encode("pass"));
        token = "jwt-token";
    }

    @Test
    void registerUser_conflict() {
        when(userRepository.findByUserName("user1")).thenReturn(Optional.of(user));
        ResponseEntity<String> resp = controller.registerUser(regReq);
        assertEquals(409, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("Username is already taken"));
    }

    @Test
    void registerUser_success() {
        when(userRepository.findByUserName("user1")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        ResponseEntity<String> resp = controller.registerUser(regReq);
        assertEquals(201, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("User registered successfully"));
    }

    @Test
    void loginUser_success() {
        when(userRepository.findByUserName("user1")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("user1")).thenReturn(token);
        ResponseEntity<String> resp = controller.loginUser(loginReq);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().contains("Login successful"));
    }

    @Test
    void loginUser_invalidPassword() {
        user.setPassword(new BCryptPasswordEncoder().encode("wrong"));
        when(userRepository.findByUserName("user1")).thenReturn(Optional.of(user));
        ResponseEntity<String> resp = controller.loginUser(loginReq);
        assertEquals(401, resp.getStatusCodeValue());
    }
}