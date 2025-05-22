package com.example.ReviewEngine.controller;

import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {
    private MockMvc mvc;
    @Mock private UserRepository userRepository;
    @InjectMocks private UserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getUserById_found() throws Exception {
        User u = new User(); u.setId(1L); u.setName("Alice"); u.setUserName("alice");
        when(userRepository.findById(1L)).thenReturn(Optional.of(u));
        mvc.perform(get("/user/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userName").value("alice"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void getAllUsers_returnsList() throws Exception {
        User u1 = new User(); u1.setId(1L); u1.setUserName("a1"); u1.setName("A");
        User u2 = new User(); u2.setId(2L); u2.setUserName("b2"); u2.setName("B");
        when(userRepository.findAll()).thenReturn(Arrays.asList(u1, u2));
        mvc.perform(get("/user/all")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}