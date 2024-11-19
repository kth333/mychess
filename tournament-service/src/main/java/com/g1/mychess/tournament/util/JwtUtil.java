package com.g1.mychess.tournament.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for handling JWT token creation, validation, and claims extraction.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationInMs = 604800000; // 7 days
    private final long refreshExpirationInMs = 2592000000L; // 30 days

    /**
     * Constructs a JwtUtil instance and initializes the secret key used for signing JWTs.
     * The secret is loaded from the environment variable "JWT_SECRET".
     */
    public JwtUtil() {
        String secret = System.getenv("JWT_SECRET");

        if (secret == null) {
            Dotenv dotenv = Dotenv.load();
            secret = dotenv.get("JWT_SECRET");

            if (secret == null) {
                throw new IllegalStateException("JWT_SECRET environment variable not set");
            }
        }
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token The JWT token to extract the username from.
     * @return The username encoded in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date of the JWT token.
     *
     * @param token The JWT token to extract the expiration date from.
     * @return The expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using a function.
     *
     * @param token The JWT token to extract the claim from.
     * @param claimsResolver The function to extract the desired claim.
     * @param <T> The type of the claim.
     * @return The extracted claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token The JWT token to check.
     * @return True if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the specified user with a 7-day expiration.
     *
     * @param userDetails The user details to include in the token.
     * @param userId The user's ID to include in the token.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("role", roles);
        return createToken(claims, userDetails.getUsername(), jwtExpirationInMs);
    }

    /**
     * Generates a refresh JWT token for the specified user with a 30-day expiration.
     *
     * @param userDetails The user details to include in the token.
     * @return The generated refresh token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpirationInMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid (not expired), false otherwise.
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the roles from the provided JWT token.
     *
     * @param token The JWT token to extract roles from.
     * @return A list of granted authorities (roles).
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("role", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Extracts the user ID from the provided JWT token.
     *
     * @param token The JWT token to extract the user ID from.
     * @return The user ID encoded in the token.
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
}
