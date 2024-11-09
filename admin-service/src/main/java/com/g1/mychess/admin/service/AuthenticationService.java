package com.g1.mychess.admin.service;

import com.g1.mychess.admin.exception.UnauthorizedActionException;
import com.g1.mychess.admin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtUtil jwtUtil;

    public AuthenticationService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        String jwtToken = extractJwtToken(request);
        return jwtUtil.extractUserId(jwtToken);
    }

    public String extractJwtToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new UnauthorizedActionException("Authorization header is missing or invalid.");
    }
}