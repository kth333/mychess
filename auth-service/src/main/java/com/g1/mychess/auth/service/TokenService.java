package com.g1.mychess.auth.service;

import com.g1.mychess.auth.exception.UserTokenException;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class responsible for handling user token operations such as generation, validation, and status updates.
 */
@Service
public class TokenService {
    private final UserTokenRepository userTokenRepository;

    /**
     * Constructs a new TokenService with the provided UserTokenRepository.
     *
     * @param userTokenRepository the repository to manage UserToken entities
     */
    public TokenService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    /**
     * Generates a new token for the specified user and token type, and saves it to the database.
     *
     * @param userId        the ID of the user for whom the token is being generated
     * @param userType      the type of the user (e.g., "ROLE_ADMIN", "ROLE_PLAYER")
     * @param tokenType     the type of the token (e.g., EMAIL_VERIFICATION)
     * @param expirationTime the expiration time for the token
     * @return the generated token string
     * @throws UserTokenException if there is an error generating or storing the token
     */
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

    /**
     * Validates a token by checking if it exists, is unused, and has not expired.
     *
     * @param token     the token to validate
     * @param tokenType the type of the token (e.g., EMAIL_VERIFICATION)
     * @return the valid UserToken entity
     * @throws UserTokenException if the token is invalid, expired, or already used
     */
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

    /**
     * Marks the provided token as used and saves it to the database.
     *
     * @param userToken the token to mark as used
     */
    public void markTokenAsUsed(UserToken userToken) {
        userToken.setUsed(true);
        userTokenRepository.save(userToken);
    }

    /**
     * Checks if the user has verified their email by checking if there is a used email verification token.
     *
     * @param userId   the ID of the user
     * @param userType the type of the user (e.g., "ROLE_ADMIN", "ROLE_PLAYER")
     * @return true if the user's email is verified, false otherwise
     */
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
