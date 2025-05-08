package com.example.ReviewEngine.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, ApiKeyAuthenticationFilter apiKeyAuthenticationFilter){
        this.jwtAuthFilter = jwtAuthFilter;
        this.apiKeyAuthenticationFilter = apiKeyAuthenticationFilter;
    }

    private static final String[] AUTH_WHITELIST = {
            "/auth/register",
            "/auth/login",
            // Swagger UI
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers(AUTH_WHITELIST).permitAll();
            auth.anyRequest().authenticated();
        });

        // Add API key filter first
        httpSecurity.addFilterBefore(apiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // Then JWT filter (will only run if API key filter didn't authenticate)
        httpSecurity.addFilterBefore(jwtAuthFilter, ApiKeyAuthenticationFilter.class);

        return httpSecurity.build();
    }
}




