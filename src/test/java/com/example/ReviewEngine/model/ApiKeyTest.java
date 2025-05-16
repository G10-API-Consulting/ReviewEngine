package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.*;

class ApiKeyTest {

    @Test
    void generateKey_hasCorrectLength() {
        String key = ApiKey.generateKey();
        assertThat(key).hasSize(36);
    }

    @Test
    void generateKey_producesValidUuid() {
        String key = ApiKey.generateKey();
        assertThatCode(() -> UUID.fromString(key))
                .doesNotThrowAnyException();
    }

    @Test
    void generateKey_isUniqueAcrossCalls() {
        String k1 = ApiKey.generateKey();
        String k2 = ApiKey.generateKey();
        assertThat(k1).isNotEqualTo(k2);
    }
}
