package com.g1.mychess.auth.service;

import com.g1.mychess.auth.exception.UserTokenException;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {
    private final UserTokenRepository userTokenRepository;

    public TokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    public String generateToken(Long userId, String userType, UserToken.TokenType tokenType, LocalDateTime expirationTime) {
        try {
            Optional<UserToken> existingToken = userTokenRepository.findByUserIdAndUserTypeAndTokenType(userId, userType, tokenType);
            existingToken.ifPresent(userTokenRepository::delete);

            String token = UUID.randomUUID().toString();

            UserToken userToken = new UserToken(
                    token,
                    expirationTime,
                    tokenType,
                    userId,
                    userType
            );

            userTokenRepository.save(userToken);

            return token;
        } catch (Exception e) {
            throw new UserTokenException("Failed to generate or store verification token.");
        }
    }

    public UserToken validateToken(String token, UserToken.TokenType tokenType) {
        UserToken userToken = userTokenRepository.findByTokenAndTokenType(token, tokenType)
                .orElseThrow(() -> new UserTokenException("Token is invalid."));

        if (userToken.isUsed()) {
            throw new UserTokenException("Token already used.");
        }

        if (userToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserTokenException("Token has expired.");
        }

        return userToken;
    }

    public void markTokenAsUsed(UserToken userToken) {
        userToken.setUsed(true);
        userTokenRepository.save(userToken);
    }

    public boolean isEmailVerified(Long userId, String userType) {
        UserToken userToken = userTokenRepository.findByUserIdAndUserTypeAndTokenTypeAndUsed(
                userId,
                userType,
                UserToken.TokenType.EMAIL_VERIFICATION,
                true
        ).orElse(null);

        return userToken != null;
    }
}