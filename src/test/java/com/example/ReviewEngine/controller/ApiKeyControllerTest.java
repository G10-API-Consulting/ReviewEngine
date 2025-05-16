package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.service.ApiKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiKeyControllerTest {

    @Mock ApiKeyService apiKeyService;
    @Mock Authentication authentication;
    @InjectMocks ApiKeyController controller;

    private User user;
    private ApiKey key;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserName("alice");
        key = new ApiKey();
        key.setKey("xyz");
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void generateApiKey_returnsKeyAndMessage() {
        when(apiKeyService.generateApiKey(user)).thenReturn(key);

        ResponseEntity<Map<String, String>> resp = controller.generateApiKey(authentication);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        Map<String, String> body = resp.getBody();
        assertThat(body).containsEntry("apiKey", "xyz");
        assertThat(body.get("message")).isNotBlank();
    }

    @Test
    void getApiKey_returnsKeyIfPresent() {
        when(apiKeyService.getActiveApiKey(user)).thenReturn(Optional.of(key));

        ResponseEntity<Map<String, String>> resp = controller.getApiKey(authentication);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).containsEntry("apiKey", "xyz");
    }

    @Test
    void getApiKey_returns404IfMissing() {
        when(apiKeyService.getActiveApiKey(user)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> resp = controller.getApiKey(authentication);

        assertThat(resp.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void revokeApiKey_returnsRevokeMessage() {
        doNothing().when(apiKeyService).revokeAllApiKeys(user);

        ResponseEntity<Map<String, String>> resp = controller.revokeApiKey(authentication);

        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).containsEntry("message", "All API keys have been revoked.");
    }
}
