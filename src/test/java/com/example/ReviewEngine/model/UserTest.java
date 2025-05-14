package com.example.ReviewEngine.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

class UserTest {

    @Test
    void gettersAndSetters(){
        User user = new User();
        user.setName("Leon");
        user.setUserName("Pitt");
        user.setPassword("pass");
        user.setRole(User.Role.CUSTOMER);
        user.setApiKey("abc123");

        assertEquals("Leon", user.getName());
        assertEquals("Pitt", user.getUserName());
        assertEquals("pass", user.getPassword());
        assertEquals(User.Role.CUSTOMER, user.getRole());
        assertEquals("abc123", user.getApiKey());
        assertNull(user.getDate());
        user.onCreate();
        assertNotNull(user.getDate());
        assertTrue(user.getDate() instanceof Date);
        user.setApiKey(null);
        assertNull(user.getApiKey());
        user.setRole(null);
        assertNull(user.getRole());
        user.setPassword(null);
        assertNull(user.getPassword());
        assertTrue(user.getDate() instanceof Date);
    }
}