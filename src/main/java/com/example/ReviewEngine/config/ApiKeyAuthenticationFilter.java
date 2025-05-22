package com.example.ReviewEngine.config;

import com.example.ReviewEngine.model.ApiKey;
import com.example.ReviewEngine.model.User;
import com.example.ReviewEngine.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private final ApiKeyService apiKeyService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final List<String> protectedPaths = Arrays.asList("/product");

    public ApiKeyAuthenticationFilter(ApiKeyService apiKeyService, HandlerExceptionResolver handlerExceptionResolver) {
        this.apiKeyService = apiKeyService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String apiKeyHeader = request.getHeader("X-API-KEY");
        String requestPath = request.getRequestURI();

        boolean isProtectedPath = protectedPaths.stream().anyMatch(requestPath::startsWith);

        if (apiKeyHeader == null && isProtectedPath) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("API key is required for this endpoint");
            return;
        }

        if (apiKeyHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Optional<ApiKey> apiKey = apiKeyService.validateApiKey(apiKeyHeader);

                if (apiKey.isPresent()) {
                    User user = apiKey.get().getUser();

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user, null, List.of() // empty list of authorities
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}