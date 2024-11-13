package com.g1.mychess.match.filter;

import com.g1.mychess.match.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * JwtRequestFilter is a custom filter that processes JWT authentication.
 * It checks for a valid JWT in the Authorization header, validates it,
 * extracts the user information, and sets the authentication context
 * for Spring Security.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructor to initialize JwtUtil dependency for JWT processing.
     *
     * @param jwtUtil Utility class for handling JWT operations.
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters incoming HTTP requests to extract and validate JWT tokens.
     * If a valid token is found, the username is extracted, and the
     * authentication context is set with the user's details.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response.
     * @param chain The filter chain to continue the request processing.
     * @throws ServletException If an error occurs during request processing.
     * @throws IOException If an input/output error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Optional<String> jwtOpt = extractJwt(request);
        if (jwtOpt.isPresent() && jwtUtil.validateToken(jwtOpt.get())) {
            String username = jwtUtil.extractUsername(jwtOpt.get());

            // Check if authentication is not set in the SecurityContext
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwtOpt.get());
                setAuthenticationContext(username, request, authorities);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     *
     * @param request The HTTP request containing the Authorization header.
     * @return An Optional containing the JWT if present, otherwise empty.
     */
    private Optional<String> extractJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return Optional.of(authorizationHeader.substring(7));
        }
        return Optional.empty();
    }

    /**
     * Sets the authentication context for the user by creating an
     * authentication token with the user's username and roles/authorities.
     *
     * @param username The username extracted from the JWT token.
     * @param request The incoming HTTP request.
     * @param authorities The list of granted authorities/roles for the user.
     */
    private void setAuthenticationContext(String username, HttpServletRequest request, List<GrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}