package com.g1.mychess.auth.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "user_tokens")
public class UserToken {

    @Id
    @NotBlank(message = "Token cannot be blank")
    @Size(max = 255, message = "Token cannot exceed 255 characters")
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @NotNull(message = "Expiration time cannot be null")
    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Token type cannot be null")
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    @NotNull(message = "User ID cannot be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotBlank(message = "User type cannot be blank")
    @Size(max = 100, message = "User type cannot exceed 100 characters")
    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "used", nullable = false)
    private boolean used;

    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    public UserToken() {}

    public UserToken(String token, LocalDateTime expirationTime, TokenType tokenType, Long userId, String userType) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userType = userType;
        this.used = false;
    }

    // Getters and Setters

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() { return userId; }

    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserType() { return userType; }

    public void setUserType(String userType) { this.userType = userType; }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
