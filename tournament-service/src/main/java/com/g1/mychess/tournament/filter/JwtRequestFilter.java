package com.g1.mychess.tournament.filter;

import com.g1.mychess.tournament.util.JwtUtil;
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
 * This filter intercepts incoming HTTP requests to extract and validate JWT tokens.
 * It sets the authentication context if the token is valid, allowing secure access to the application.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructs a new JwtRequestFilter with the specified JwtUtil.
     *
     * @param jwtUtil the utility class for extracting and validating JWT tokens
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method is executed for each HTTP request. It extracts the JWT from the Authorization header,
     * validates the token, and sets the authentication in the security context if the token is valid.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param chain the filter chain
     * @throws ServletException if an exception occurs during the filtering process
     * @throws IOException if an input or output exception occurs during the filtering process
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
