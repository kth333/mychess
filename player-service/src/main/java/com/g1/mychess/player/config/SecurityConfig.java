package com.g1.mychess.player.config;

import com.g1.mychess.player.filter.JwtRequestFilter;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for setting up authentication and authorization settings.
 * <p>
 * This class configures CORS settings and HTTP security for the application, including JWT token validation and stateless session management.
 * <p>
 * It defines two main beans:
 * <ul>
 * <li>{@link #corsConfigurationSource()} - Configures CORS settings for allowed origins, methods, and headers.</li>
 * <li>{@link #securityFilterChain(HttpSecurity)} - Configures HTTP security including authentication, authorization, and JWT filter setup.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructs a new {@link SecurityConfig} instance with the provided {@link JwtRequestFilter}.
     *
     * @param jwtRequestFilter the JWT request filter used for token validation
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configures CORS settings for the application.
     * <p>
     * This method sets allowed origins, allowed HTTP methods, and allowed headers. It also allows credentials
     * to be sent in cross-origin requests and configures the source for CORS settings.
     *
     * @return the CORS configuration source used by the application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://54.251.32.169", "http://localhost:3000", "https://www.mychesss.com",
                "https://mychesss.com", "http://mychesss.com", "http://www.mychesss.com",
                "https://54.251.32.169", "http://52.221.193.231", "https://52.221.193.231"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * Configures HTTP security for the application, including JWT token validation and stateless session management.
     * <p>
     * This method disables CSRF protection, enables CORS with the configured settings, and sets authorization rules.
     * <p>
     * It also adds the {@link JwtRequestFilter} to the security filter chain to validate JWT tokens before allowing access to protected endpoints.
     *
     * @param http the {@link HttpSecurity} instance used to configure HTTP security
     * @return the configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs while configuring HTTP security
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/player/**").permitAll() // Allow public access to player endpoints
                        .requestMatchers("/api/v1/profile/**").permitAll() // Allow public access to profile endpoints
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }
}
