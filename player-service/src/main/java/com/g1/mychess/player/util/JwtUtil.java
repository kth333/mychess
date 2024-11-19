package com.g1.mychess.player.util;

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
 * Utility class for handling JSON Web Tokens (JWT).
 * Provides methods for generating, validating, and extracting claims from JWT tokens.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationInMs = 604800000; // 7 days
    private final long refreshExpirationInMs = 2592000000L; // 30 days

    /**
     * Constructor that loads the JWT secret key from an environment variable or .env file.
     * Throws an exception if the secret key is not set.
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
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token from which to extract the username.
     * @return the username (subject) from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token from which to extract the expiration date.
     * @return the expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token from which to extract the claim.
     * @param claimsResolver the function that extracts the claim.
     * @param <T> the type of the claim.
     * @return the extracted claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token from which to extract the claims.
     * @return the claims present in the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the JWT token has expired.
     *
     * @param token the JWT token to check.
     * @return true if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the given user details and user ID.
     *
     * @param userDetails the user details used to create the token.
     * @param userId the ID of the user.
     * @return a generated JWT token.
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
     * Generates a refresh token for the given user details.
     *
     * @param userDetails the user details used to create the refresh token.
     * @return a generated refresh token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpirationInMs);
    }

    /**
     * Creates a JWT token with the specified claims, subject, and expiration time.
     *
     * @param claims the claims to be included in the token.
     * @param subject the subject (username) for the token.
     * @param expirationTime the expiration time of the token in milliseconds.
     * @return a generated JWT token.
     */
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
     * Validates a JWT token by checking its expiration date.
     *
     * @param token the JWT token to validate.
     * @return true if the token is valid (not expired), false otherwise.
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the roles (authorities) from the given JWT token.
     *
     * @param token the JWT token from which to extract the roles.
     * @return a list of granted authorities (roles) from the token.
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("role", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Extracts the user ID from the given JWT token.
     *
     * @param token the JWT token from which to extract the user ID.
     * @return the user ID from the token.
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
}