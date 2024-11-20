package com.g1.mychess.player.filter;

import com.g1.mychess.player.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
 * JwtRequestFilter is a custom filter that processes incoming HTTP requests to check for a valid JWT token
 * in the "Authorization" header. If a valid token is found, the filter extracts the username and roles
 * from the token and sets the authentication context for the current request.
 *
 * This filter extends OncePerRequestFilter to ensure that it is executed once per request in the filter chain.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    /**
     * Constructs a JwtRequestFilter with the provided JwtUtil instance.
     *
     * @param jwtUtil the JwtUtil used to extract and validate the JWT
     */
    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Processes the incoming HTTP request to extract and validate the JWT from the "Authorization" header.
     * If a valid JWT is found, the filter creates an authentication token with the extracted username and roles,
     * and sets the authentication context for the request.
     *
     * @param request the HTTP request to process
     * @param response the HTTP response to send
     * @param chain the filter chain to continue the processing of the request
     * @throws ServletException if there is a servlet-related issue
     * @throws IOException if an I/O error occurs
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