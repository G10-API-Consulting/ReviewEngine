package com.example.ReviewEngine.service;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.ApiKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiKeyServiceTest {

    private ApiKeyRepository repo;
    private ApiKeyService service;
    private User user;

    @BeforeEach
    void setUp() {
        repo = mock(ApiKeyRepository.class);
        service = new ApiKeyService(repo);
        user = new User();
        user.setUserName("alice");
    }

    @Test
    void generateApiKey_deactivatesPreviousAndCreatesNew() {
        ApiKey oldKey = new ApiKey();
        oldKey.setKey("old");
        oldKey.setUser(user);
        when(repo.findByUserAndIsActiveTrue(user)).thenReturn(Collections.singletonList(oldKey));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        ApiKey newKey = service.generateApiKey(user);

        // old key is deactivated
        assertThat(oldKey.isActive()).isFalse();
        // new key has correct user and is active
        assertThat(newKey.getUser()).isEqualTo(user);
        assertThat(newKey.isActive()).isTrue();
        assertThat(newKey.getKey()).isNotBlank();

        // verify save() called for both old and new
        verify(repo, times(2)).save(any(ApiKey.class));
    }

    @Test
    void validateApiKey_filtersInactive() {
        ApiKey key = new ApiKey();
        key.setKey("k");
        key.setActive(false);
        when(repo.findByKey("k")).thenReturn(Optional.of(key));

        Optional<ApiKey> result = service.validateApiKey("k");
        assertThat(result).isEmpty();
    }
}
