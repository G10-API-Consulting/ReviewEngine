package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.dto.LoginRequest;
import com.example.ReviewEngine.dto.RegisterRequest;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import com.example.ReviewEngine.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.findByUserName(request.getUserName()).isPresent()) {
            return ResponseEntity.status(409).body("Username is already taken");
        }

        User user = new User();
        user.setName(request.getName());
        user.setUserName(request.getUserName());

        // Hash the password
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));

        user.setApiKey(UUID.randomUUID().toString());

        userRepository.save(user);
        return ResponseEntity.status(201).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest request) {
        User existingUser = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(request.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtService.generateToken(existingUser.getUserName());
        return ResponseEntity.ok("Login successful, JWT: " + token);
    }
}