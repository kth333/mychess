package com.g1.mychess.tournament.service;

import com.g1.mychess.tournament.exception.UnauthorizedActionException;
import com.g1.mychess.tournament.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling authentication-related operations,
 * including extracting user information from JWT tokens in the request header.
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    /**
     * Constructs a new {@link AuthenticationService} with the given {@link JwtUtil}.
     *
     * @param jwtUtil the {@link JwtUtil} used to extract user information from JWT tokens
     */
    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Extracts the user ID from the JWT token present in the HTTP request.
     *
     * @param request the HTTP request containing the authorization header
     * @return the user ID extracted from the JWT token
     * @throws UnauthorizedActionException if the authorization header is missing or invalid
     */
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return jwtUtil.extractUserId(jwtToken);
        }
        throw new UnauthorizedActionException("Authorization header is missing or invalid.");
    }

    /**
     * Extracts the JWT token from the authorization header in the HTTP request.
     *
     * @param request the HTTP request containing the authorization header
     * @return the JWT token as a {@link String}
     * @throws UnauthorizedActionException if the authorization header is missing or invalid
     */
    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedActionException("Authorization header is missing or invalid.");
    }
}
