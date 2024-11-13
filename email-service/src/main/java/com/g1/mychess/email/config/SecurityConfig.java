package com.g1.mychess.email.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuration class for Spring Security settings.
 * <p>
 * This class configures CORS, disables CSRF for stateless security, and sets access controls for various endpoints.
 * The session management is configured to be stateless as the application is intended to be a REST API.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configures Cross-Origin Resource Sharing (CORS) settings.
     * <p></p>
     * Allows specific origins and methods for cross-origin requests to ensure the application
     * can communicate with the front-end.
     *
     * @return a CorsConfigurationSource containing CORS configuration for allowed origins, methods, and headers.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://54.251.32.169", "http://localhost:3000", "https://mychesss.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Configures the main security filter chain for the application.
     * <p>
     * - Disables CSRF as this is a stateless API.
     * - Configures CORS to allow specified origins and methods.
     * - Sets authorization rules, permitting access to the email API while protecting other endpoints.
     * - Configures session management to be stateless.
     *
     * @param http the HttpSecurity instance to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while building the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/email/**").permitAll() // Allow public access to email endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}