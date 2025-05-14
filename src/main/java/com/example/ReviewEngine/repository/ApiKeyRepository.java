package com.example.ReviewEngine.repository;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    Optional<ApiKey> findByKey(String key);
    List<ApiKey> findByUserAndIsActiveTrue(User user);
}
