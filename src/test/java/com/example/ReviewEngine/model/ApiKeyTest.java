package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ApiKeyTest {
    @Test
    void generateKey_returnsUuidString() {
        String key1 = ApiKey.generateKey();
        String key2 = ApiKey.generateKey();
        assertNotNull(key1);
        assertNotNull(key2);
        assertNotEquals(key1, key2);
        assertEquals(36, key1.length());

    }

    @Test
    void gettersAndSetters(){
        ApiKey apiKey = new ApiKey();
        apiKey.setKey("myKey");
        apiKey.setActive(false);

        User user = new User();
        user.setUserName("doo");
        apiKey.setUser(user);
        assertEquals("myKey", apiKey.getKey());
        assertFalse(apiKey.isActive());
        assertSame(user, apiKey.getUser());
    }


}