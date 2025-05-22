package com.example.ReviewEngine.service;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.ApiKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyService {
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Transactional
    public ApiKey generateApiKey(User user) {
        List<ApiKey> activeKeys = apiKeyRepository.findByUserAndIsActiveTrue(user);
        activeKeys.forEach(key -> {
            key.setActive(false);
            apiKeyRepository.save(key);
        });

        ApiKey apiKey = new ApiKey();
        apiKey.setKey(ApiKey.generateKey());
        apiKey.setUser(user);
        apiKey.setActive(true);
        return apiKeyRepository.save(apiKey);
    }

    public Optional<ApiKey> getActiveApiKey(User user) {
        return apiKeyRepository.findByUserAndIsActiveTrue(user).stream().findFirst();
    }

    @Transactional
    public void revokeAllApiKeys(User user) {
        List<ApiKey> activeKeys = apiKeyRepository.findByUserAndIsActiveTrue(user);
        activeKeys.forEach(key -> {
            key.setActive(false);
            apiKeyRepository.save(key);
        });
    }

    public Optional<ApiKey> validateApiKey(String key) {
        return apiKeyRepository.findByKey(key).filter(ApiKey::isActive);
    }
}
