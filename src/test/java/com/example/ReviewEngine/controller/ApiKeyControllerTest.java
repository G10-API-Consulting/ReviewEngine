package com.example.ReviewEngine.controller;
import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.service.ApiKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiKeyControllerTest {
    @Mock
    private ApiKeyService apiKeyService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ApiKeyController apiKeyController;

    private User user;
    private ApiKey apiKey;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserName("TestUser");
        apiKey = new ApiKey();
        apiKey.setKey("abc123");
        when(authentication.getPrincipal()).thenReturn(user);

    }

    @Test
    void generateApiKey_success(){
        when(apiKeyService.generateApiKey(user)).thenReturn(apiKey);
        ResponseEntity<?> response = apiKeyController.generateApiKey(authentication);
        assertEquals(200, response.getStatusCodeValue());
        var body = (java.util.Map<String,String>) response.getBody();
        assertNotNull(body);
        assertEquals("abc123", body.get("apiKey"));
    }

    @Test
    void getApiKey_found() {
        when(apiKeyService.getActiveApiKey(user)).thenReturn(Optional.of(apiKey));
        ResponseEntity<?> response = apiKeyController.getApiKey(authentication);
        assertEquals(200, response.getStatusCodeValue());
        var body = (java.util.Map<String,String>) response.getBody();
        assertEquals("abc123", body.get("apiKey"));
    }

    @Test
    void getApiKey_notFound() {
        when(apiKeyService.getActiveApiKey(user)).thenReturn(Optional.empty());
        ResponseEntity<?> response = apiKeyController.getApiKey(authentication);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void revokeApiKey() {
        doNothing().when(apiKeyService).revokeAllApiKeys(user);
        ResponseEntity<?> response = apiKeyController.revokeApiKey(authentication);
        assertEquals(200, response.getStatusCodeValue());
        var body = (java.util.Map<String,String>) response.getBody();
        assertEquals("All API keys have been revoked.", body.get("message"));
    }
}