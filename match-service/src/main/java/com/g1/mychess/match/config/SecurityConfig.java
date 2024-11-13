package com.g1.mychess.match.config;

import com.g1.mychess.match.filter.JwtRequestFilter;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Security configuration class for the application.
 *
 * <p>This class handles the security configurations for the web application, including:
 * <ul>
 *     <li>Defining roles and access control for specific endpoints.</li>
 *     <li>Setting up JWT authentication filter.</li>
 *     <li>Configuring CORS (Cross-Origin Resource Sharing) for the application.</li>
 *     <li>Ensuring stateless sessions with JWT tokens for authentication.</li>
 * </ul>
 * </p>
 *
 * <p>It integrates with the Spring Security framework to secure the application from unauthorized access
 * and to ensure that requests are properly authenticated and authorized before access is granted to
 * protected resources.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Constructs a {@link SecurityConfig} instance.
     *
     * @param jwtRequestFilter the JWT request filter to be applied to incoming HTTP requests.
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Configures security settings such as CORS, JWT filter, and access control.
     *
     * @param http HttpSecurity instance to configure security settings.
     * @return SecurityFilterChain for the application.
     * @throws Exception if there is any error during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/matches/admin/**").hasRole("ADMIN")  // Only ADMIN can access
                        .requestMatchers("/api/v1/matches/player/**").hasRole("PLAYER")  // Only PLAYER can access
                        .requestMatchers("/api/v1/matches/**").permitAll()  // Public access
                        .anyRequest().authenticated()           // Protect other endpoints
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Configures CORS to allow specific origins and headers.
     *
     * @return CorsConfigurationSource with allowed origins, methods, and headers.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://54.251.32.169", "http://localhost:3000", "https://www.mychesss.com", "https://mychesss.com", "http://mychesss.com", "http://www.mychesss.com", "https://54.251.32.169", "http://52.221.193.231", "https://52.221.193.231"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}