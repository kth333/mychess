package com.g1.mychess.auth.model;

import com.g1.mychess.user.model.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    private TokenType tokenType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "used", nullable = false)
    private boolean used;

    public enum TokenType {
        EMAIL_VERIFICATION,
        PASSWORD_RESET
    }

    public VerificationToken() {}

    public VerificationToken(String token, LocalDateTime expirationTime, TokenType tokenType, User user) {
        this.token = token;
        this.expirationTime = expirationTime;
        this.tokenType = tokenType;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
