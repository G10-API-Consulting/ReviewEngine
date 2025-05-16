package com.example.ReviewEngine.service;
import com.example.ReviewEngine.config.JwtAuthenticationFilter;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.repository.UserRepository;
import com.example.ReviewEngine.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain chain;

    @Mock
    JwtService jwtService;

    @Mock
    UserRepository userRepo;

    @InjectMocks
    JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void whenNoAuthorizationHeader_chainContinuesWithoutAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userRepo);
    }

    @Test
    void whenInvalidBearerFormat_chainContinuesWithoutAuth() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("NotBearer token123");
        filter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verifyNoInteractions(jwtService, userRepo);
    }

    @Test
    void whenValidToken_setsAuthenticationAndContinues() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer valid123");
        when(jwtService.getUsernameFromToken("valid123")).thenReturn("bob");
        when(jwtService.validateToken("valid123")).thenReturn(true);

        User bob = new User();
        bob.setUserName("bob");
        when(userRepo.findByUserName("bob")).thenReturn(Optional.of(bob));
        filter.doFilter(request, response, chain);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isNotNull();
        assertThat(auth.getPrincipal()).isSameAs(bob);
        verify(chain).doFilter(request, response);
    }

    @Test
    void whenTokenInvalid_skipsAuthenticationButContinues() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer badtoken");
        when(jwtService.getUsernameFromToken("badtoken")).thenReturn("alice");
        when(userRepo.findByUserName("alice")).thenReturn(Optional.of(new User()));
        when(jwtService.validateToken("badtoken")).thenReturn(false);

        filter.doFilter(request, response, chain);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(chain).doFilter(request, response);
    }
}
