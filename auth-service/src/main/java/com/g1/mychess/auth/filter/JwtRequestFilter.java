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

/**
 * JwtRequestFilter is a Spring Security filter that processes incoming HTTP requests
 * to validate JWT tokens in the Authorization header. If the token is valid, the filter
 * extracts the username and roles from the token and sets the authentication context for
 * the current request.
 * <p>
 * This filter is executed once per request and ensures that the JWT token is validated
 * and the user is authenticated before proceeding with the request.
 * </p>
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructs a new JwtRequestFilter with the provided JwtUtil.
     *
     * @param jwtUtil The utility class responsible for handling JWT token operations.
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method filters the incoming HTTP request, extracting and validating the JWT token
     * from the Authorization header. If the token is valid, it sets the authentication context
     * for the current user based on the extracted username and roles.
     *
     * @param request The incoming HTTP request.
     * @param response The outgoing HTTP response.
     * @param chain The filter chain to pass the request along.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Extract the JWT from the Authorization header
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt); // Extract username from token
        }

        // Check if username is not null and there is no existing authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Validate JWT token
            if (jwtUtil.validateToken(jwt)) {
                // Extract roles/authorities from the JWT
                List<GrantedAuthority> authorities = jwtUtil.extractRoles(jwt);

                // Create the authentication token with extracted authorities and set it in the context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, authorities); // No need for password here
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}

