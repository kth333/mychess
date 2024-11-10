package com.g1.mychess.auth.util;

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
 * Utility class for handling JWT (JSON Web Token) operations such as token creation, validation, and extraction of claims.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationInMs = 604800000; // 7 days
    private final long refreshExpirationInMs = 2592000000L; // 30 days

    /**
     * Constructor that initializes the secret key from an environment variable or a .env file.
     * Throws an IllegalStateException if the JWT_SECRET environment variable is not set.
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
     * @param token the JWT token
     * @return the username from the token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token           the JWT token
     * @param claimsResolver  function to extract the claim
     * @param <T>             the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return all claims from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, otherwise false
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for the given user details with a specified expiration time.
     *
     * @param userDetails the user details (including username and roles)
     * @param userId      the user ID
     * @return the generated JWT token
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
     * @param userDetails the user details
     * @return the generated refresh token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshExpirationInMs);
    }

    /**
     * Creates a JWT token with the specified claims, subject, and expiration time.
     *
     * @param claims        the claims to include in the token
     * @param subject       the subject (usually the username)
     * @param expirationTime the expiration time of the token in milliseconds
     * @return the created JWT token
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
     * Validates a JWT token to ensure it is not expired and is valid.
     *
     * @param token the JWT token
     * @return true if the token is valid, otherwise false
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the roles from the JWT token.
     *
     * @param token the JWT token
     * @return a list of granted authorities (roles) from the token
     */
    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("role", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    /**
     * Extracts the user ID from the JWT token.
     *
     * @param token the JWT token
     * @return the user ID from the token
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
}
