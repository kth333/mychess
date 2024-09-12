package com.g1.mychess.tournament.filter;

import com.g1.mychess.tournament.dto.AuthResponseDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final WebClient.Builder webClientBuilder;

    public JwtRequestFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String token = null;

        // Check if the Authorization header contains a Bearer token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        // If there is a token, validate it with the auth-service
        if (token != null) {
            // Call auth-service to validate the token
            try {
                AuthResponseDTO authResponseDTO = webClientBuilder.build()
                        .post()
                        .uri("http://auth-service:8080/api/v1/auth/validate-jwt")
                        .bodyValue(token)  // Send token to auth-service
                        .retrieve()
                        .bodyToMono(AuthResponseDTO.class)
                        .block();  // Blocking for simplicity (use async/reactive in production)

                // If valid, set the security context
                if (authResponseDTO != null) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            authResponseDTO.getUsername(), null, authResponseDTO.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // Invalid token
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
