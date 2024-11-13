package com.g1.mychess.auth.filter;

import com.g1.mychess.auth.util.JwtUtil;
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
 * JwtRequestFilter is a custom filter responsible for JWT authentication.
 * It processes the incoming HTTP requests to check for a valid JWT token,
 * validates it, extracts the user's information, and sets the authentication context
 * for Spring Security.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    /**
     * JwtUtil is a utility class used for handling JWT operations, including
     * extracting user information and validating the token.
     */
    private final JwtUtil jwtUtil;

    /**
     * Constructor to initialize the JwtRequestFilter with JwtUtil dependency.
     *
     * @param jwtUtil The JwtUtil instance used for JWT operations.
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Filters the incoming HTTP requests to extract and validate the JWT token.
     * If a valid token is found, it extracts the username and roles, and sets
     * the authentication context for Spring Security.
     *
     * @param request The incoming HTTP request.
     * @param response The HTTP response to be sent.
     * @param chain The filter chain to continue processing the request.
     * @throws ServletException If an error occurs while processing the request.
     * @throws IOException If an input/output error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        Optional<String> jwtOpt = extractJwt(request);

        // If JWT is present and valid, set authentication context
        if (jwtOpt.isPresent() && jwtUtil.validateToken(jwtOpt.get())) {
            String username = jwtUtil.extractUsername(jwtOpt.get());

            // Set authentication context if not already set
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwtOpt.get());
                setAuthenticationContext(username, request, authorities);
            }
        }

        // Continue processing the request
        chain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the HTTP request.
     * If the header contains a valid "Bearer" token, it returns the token;
     * otherwise, it returns an empty Optional.
     *
     * @param request The HTTP request containing the Authorization header.
     * @return An Optional containing the JWT if present, otherwise empty.
     */
    private Optional<String> extractJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return Optional.of(authorizationHeader.substring(7));  // Extract JWT token
        }
        return Optional.empty();  // Return empty if no valid token found
    }

    /**
     * Sets the authentication context by creating an authentication token with
     * the username and roles/authorities extracted from the JWT token.
     *
     * @param username The username extracted from the JWT token.
     * @param request The incoming HTTP request.
     * @param authorities The list of granted authorities for the user.
     */
    private void setAuthenticationContext(String username, HttpServletRequest request, List<GrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));  // Set request details
        SecurityContextHolder.getContext().setAuthentication(authToken);  // Set the authentication in the context
    }
}

