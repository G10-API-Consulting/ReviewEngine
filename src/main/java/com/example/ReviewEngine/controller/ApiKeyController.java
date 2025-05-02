package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api-key")
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateApiKey(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ApiKey apiKey = apiKeyService.generateApiKey(user);

        Map<String, String> response = new HashMap<>();
        response.put("apiKey", apiKey.getKey());
        response.put("message", "API key generated successfully. Any previous active keys have been deactivated.");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getApiKey(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Optional<ApiKey> apiKey = apiKeyService.getActiveApiKey(user);

        if (apiKey.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("apiKey", apiKey.get().getKey());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> revokeApiKey(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        apiKeyService.revokeAllApiKeys(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "All API keys have been revoked.");

        return ResponseEntity.ok(response);
    }
}
