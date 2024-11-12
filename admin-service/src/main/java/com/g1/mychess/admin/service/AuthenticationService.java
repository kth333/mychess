package com.g1.mychess.admin.service;

import com.g1.mychess.admin.exception.UnauthorizedActionException;
import com.g1.mychess.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
 * Service class that handles authentication-related actions, including extracting and validating JWT tokens
 * from incoming HTTP requests. It provides methods to extract user IDs and JWT tokens from requests.
 */
@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    /**
     * Constructor that initializes the AuthenticationService with the JwtUtil instance.
     *
     * @param jwtUtil The JwtUtil instance used for extracting information from JWT tokens.
     */
    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Extracts the user ID from the JWT token present in the HTTP request's Authorization header.
     *
     * @param request The HTTP request containing the Authorization header with the JWT token.
     * @return The user ID extracted from the JWT token.
     * @throws UnauthorizedActionException If the Authorization header is missing or invalid.
     */
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String jwtToken = extractJwtToken(request);
        return jwtUtil.extractUserId(jwtToken);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * The token must be prefixed with "Bearer ".
     *
     * @param request The HTTP request containing the Authorization header.
     * @return The extracted JWT token.
     * @throws UnauthorizedActionException If the Authorization header is missing or not properly formatted.
     */
    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedActionException("Authorization header is missing or invalid.");
    }
}