package com.example.ReviewEngine.services;
import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.ApiKeyRepository;
import com.example.ReviewEngine.service.ApiKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.List;


class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository repo;

    @InjectMocks
    private ApiKeyService service;

    private User user;
    private ApiKey existingKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserName("user1");
        existingKey = new ApiKey();
        existingKey.setKey("good");
        existingKey.setUser(user);
        existingKey.setActive(true);
    }

    @Test
    void generateApiKey_deactivatesOldAndSavesNew() {
        when(repo.findByUserAndIsActiveTrue(user)).thenReturn(List.of(existingKey));
        when(repo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ApiKey generated = service.generateApiKey(user);
        assertFalse(existingKey.isActive());
        verify(repo).save(existingKey);


        assertNotNull(generated.getKey());
        assertTrue(generated.isActive());
        assertSame(user, generated.getUser());
        verify(repo).save(generated);
    }

    @Test
    void getActiveApiKey_whenNone_returnsEmpty() {
        when(repo.findByUserAndIsActiveTrue(user)).thenReturn(Collections.emptyList());
        assertTrue(service.getActiveApiKey(user).isEmpty());
    }

    @Test
    void validateApiKey_filtersInactive() {
        ApiKey key = new ApiKey();
        key.setKey("isi");
        key.setActive(false);
        when(repo.findByKey("isi")).thenReturn(Optional.of(key));
        assertTrue(service.validateApiKey("isi").isEmpty());
    }
}