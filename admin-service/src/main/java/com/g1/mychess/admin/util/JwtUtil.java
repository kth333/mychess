package com.g1.mychess.admin.util;

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
 * Utility class for handling JWT token creation, validation, and extraction of claims.
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long jwtExpirationInMs = 604800000; // 7 days
    private final long refreshExpirationInMs = 2592000000L; // 30 days

    /**
     * Constructor that initializes the JwtUtil class by loading the JWT secret key
     * from the environment variables or a .env file.
     *
     * @throws IllegalStateException if the JWT_SECRET environment variable is not set.
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
     * Extracts the username (subject) from the provided JWT token.
     *
     * @param token the JWT token string.
     * @return the username (subject) contained in the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the expiration date from the provided JWT token.
     *
     * @param token the JWT token string.
     * @return the expiration date of the token.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the provided JWT token.
     *
     * @param <T> the type of the claim.
     * @param token the JWT token string.
     * @param claimsResolver the function to extract the desired claim from Claims.
     * @return the extracted claim of type T.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     *
     * @param token the JWT token string.
     * @return a Claims object containing the parsed claims from the token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    /**
     * Checks if the provided JWT token is expired.
     *
     * @param token the JWT token string.
     * @return true if the token is expired, false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a JWT token for a user with the specified details and user ID.
     *
     * @param userDetails The user details object (containing username, roles).
     * @param userId      The user ID to be included in the token.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = createClaims(userDetails, userId);
        return createToken(claims, userDetails.getUsername(), jwtExpirationInMs);
    }

    /**
     * Generates a refresh JWT token for a user.
     *
     * @param userDetails The user details object (containing username).
     * @return The generated refresh JWT token.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = createClaims(userDetails, null);
        return createToken(claims, userDetails.getUsername(), refreshExpirationInMs);
    }

    /**
     * Creates the JWT token with the given claims, subject, and expiration time.
     *
     * @param claims         The claims to be included in the token.
     * @param subject        The subject (usually username) to be included in the token.
     * @param expirationTime The expiration time of the token in milliseconds.
     * @return The generated JWT token.
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
     * Validates the JWT token by checking if it is expired.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid (not expired), otherwise false.
     */
    public Boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates the claims map for the JWT token from the user details and user ID.
     *
     * @param userDetails The user details object containing username and roles.
     * @param userId      The user ID to be included in the claims (can be null).
     * @return A map containing the claims.
     */
    private Map<String, Object> createClaims(UserDetails userDetails, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        if (userId != null) {
            claims.put("userId", userId);
        }
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("role", roles);
        return claims;
    }

    /**
     * Extracts the roles from the JWT token.
     *
     * @param token The JWT token.
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
     * Extracts the user ID from the JWT token.
     *
     * @param token The JWT token.
     * @return The user ID stored in the token.
     */
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
}