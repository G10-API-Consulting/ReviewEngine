package com.example.ReviewEngine.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = {
            "/auth/register",  // Uppdatera denna rad från "/auth/register"
            "/auth/login",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui",
            "/swagger-ui/",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/product",
            "/product/save",
            "/product/id",
            "/user",
            "/user/{id}",
            "/user/all",
            "/**"
    };

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers(AUTH_WHITELIST).permitAll();
            auth.anyRequest().authenticated();
        });

        return httpSecurity.build();
    }
}



