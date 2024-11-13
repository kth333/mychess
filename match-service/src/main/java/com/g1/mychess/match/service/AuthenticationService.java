package com.g1.mychess.match.service;

import com.g1.mychess.match.exception.UnauthorizedActionException;
import com.g1.mychess.match.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * Service class for handling authentication-related operations.
 * <p>
 * This service provides methods for extracting user information from HTTP requests
 * based on the JWT (JSON Web Token) present in the "Authorization" header. The class
 * utilizes the {@link JwtUtil} utility to handle JWT token parsing and extraction.
 * </p>
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    /**
     * Constructs an {@link AuthenticationService} with the provided {@link JwtUtil}.
     * <p>
     * The {@link JwtUtil} instance is used to extract user details from JWT tokens.
     * </p>
     *
     * @param jwtUtil The {@link JwtUtil} utility to extract data from JWT tokens.
     */
    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Extracts the user ID from the JWT token in the HTTP request.
     * <p>
     * The method looks for the "Authorization" header, retrieves the JWT token, and
     * extracts the user ID. If the header is missing or invalid, an {@link UnauthorizedActionException}
     * is thrown.
     * </p>
     *
     * @param request The HTTP request containing the "Authorization" header.
     * @return The extracted user ID from the JWT token.
     * @throws UnauthorizedActionException If the "Authorization" header is missing or invalid.
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
     * Extracts the JWT token from the HTTP request.
     * <p>
     * The method looks for the "Authorization" header and retrieves the JWT token
     * by removing the "Bearer " prefix. If the header is missing or invalid,
     * an {@link UnauthorizedActionException} is thrown.
     * </p>
     *
     * @param request The HTTP request containing the "Authorization" header.
     * @return The extracted JWT token.
     * @throws UnauthorizedActionException If the "Authorization" header is missing or invalid.
     */
    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedActionException("Authorization header is missing or invalid.");
    }
}