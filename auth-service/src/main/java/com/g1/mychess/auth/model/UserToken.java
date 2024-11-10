package com.g1.mychess.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
 * Represents a token used for authentication actions, such as email verification
 * and password reset, in the MyChess application.
 */
@Entity
@Table(name = "user_tokens")
public class UserToken {

    /**
     * The unique token string. Used to verify actions like email or password reset.
     */
    @Id
    @NotBlank(message = "Token cannot be blank")
    @Size(max = 255, message = "Token cannot exceed 255 characters")
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    /**
     * The expiration time of the token. Actions using this token are valid until this time.
     */
    @NotNull(message = "Expiration time cannot be null")
    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    /**
     * Specifies the type of token, such as EMAIL_VERIFICATION or PASSWORD_RESET.
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Token type cannot be null")
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    /**
     * The ID of the user associated with this token.
     */
    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The type of user, e.g., PLAYER or ADMIN, who owns this token.
     */
    @NotBlank(message = "User type cannot be blank")
    @Size(max = 100, message = "User type cannot exceed 100 characters")
    @Column(name = "user_type", nullable = false)
    private String userType;

    /**
     * Indicates if the token has already been used.
     */
    @Column(name = "used", nullable = false)
    private boolean used;

    /**
     * Enum representing the types of tokens available: EMAIL_VERIFICATION or PASSWORD_RESET.
     */
    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    /**
     * Default constructor for JPA.
     */
    public UserToken() {}

    /**
     * Constructs a new UserToken with specified values.
     *
     * @param token the token string
     * @param expirationTime the time when the token expires
     * @param tokenType the type of the token
     * @param userId the ID of the user this token belongs to
     * @param userType the type of the user (e.g., ADMIN or MEMBER)
     */
    public UserToken(String token, LocalDateTime expirationTime, TokenType tokenType, Long userId, String userType) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userType = userType;
        this.used = false;
    }

    // Getters and Setters

    /**
     * Gets the token string.
     * @return the token string
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token string.
     * @param token the token string
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gets the expiration time of the token.
     * @return the expiration time
     */
    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    /**
     * Sets the expiration time of the token.
     * @param expirationTime the expiration time
     */
    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Gets the token type.
     * @return the token type
     */
    public TokenType getTokenType() {
        return tokenType;
    }

    /**
     * Sets the token type.
     * @param tokenType the token type
     */
    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Gets the ID of the user associated with this token.
     * @return the user ID
     */
    public Long getUserId() { return userId; }

    /**
     * Sets the user ID associated with this token.
     * @param userId the user ID
     */
    public void setUserId(Long userId) { this.userId = userId; }

    /**
     * Gets the user type (e.g., ADMIN or MEMBER) for the associated user.
     * @return the user type
     */
    public String getUserType() { return userType; }

    /**
     * Sets the user type.
     * @param userType the user type
     */
    public void setUserType(String userType) { this.userType = userType; }

    /**
     * Checks if the token has been used.
     * @return true if used, false otherwise
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Marks the token as used or unused.
     * @param used true if used, false otherwise
     */
    public void setUsed(boolean used) {
        this.used = used;
    }
}
