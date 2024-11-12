package com.g1.mychess.admin.filter;

import com.g1.mychess.admin.util.JwtUtil;
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

/**
 * JwtRequestFilter is a custom filter that intercepts HTTP requests to validate JWT tokens.
 * It extracts the JWT from the request header, validates it, and if valid, sets the authentication
 * context for the current user with the appropriate roles/authorities.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructor for JwtRequestFilter.
     *
     * @param jwtUtil the JwtUtil instance used for extracting and validating the JWT
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method is called on each request to filter the JWT token.
     * It checks if the request contains a valid JWT token, and if so, sets the authentication context
     * with the extracted user details and roles.
     *
     * @param request the incoming HTTP request
     * @param response the HTTP response
     * @param chain the filter chain to continue the filter process
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs during the filter process
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        String jwt = extractJwt(authorizationHeader);
        String username = null;

        if (jwt != null) {
            username = jwtUtil.extractUsername(jwt); // Extract username from the token
        }

        // If username is found and there is no existing authentication, validate the token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(jwt)) {
                setAuthenticationContext(request, username, jwt);
            }
        }

        // Continue with the filter chain
        chain.doFilter(request, response);
    }

    /**
     * Extracts the JWT from the Authorization header if it is present.
     *
     * @param authorizationHeader the value of the Authorization header
     * @return the JWT token or null if the header does not contain a Bearer token
     */
    private String extractJwt(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Extract JWT token
        }
        return null;
    }

    /**
     * Sets the authentication context for the current request if the JWT is valid.
     *
     * @param request the incoming HTTP request
     * @param username the username extracted from the JWT token
     * @param jwt the JWT token
     */
    private void setAuthenticationContext(HttpServletRequest request, String username, String jwt) {
        // Extract roles/authorities from the JWT
        List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwt);

        // Create the authentication token with extracted authorities
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, authorities); // No password needed

        // Set request details and set the authentication in the security context
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}