package com.g1.mychess.auth.repository;

import com.g1.mychess.auth.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on UserToken entities.
 * Provides methods to retrieve user tokens based on various criteria.
 */
public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    /**
     * Finds a user token by user ID, user type, and token type.
     *
     * @param userId   the user ID associated with the token
     * @param userType the type of the user (e.g., "ROLE_ADMIN", "ROLE_PLAYER")
     * @param tokenType the type of the token (e.g., EMAIL_VERIFICATION)
     * @return an Optional containing the found UserToken, or empty if no token matches
     */
    Optional<UserToken> findByUserIdAndUserTypeAndTokenType(Long userId, String userType, UserToken.TokenType tokenType);

    /**
     * Finds a user token by user ID, user type, token type, and the 'used' status.
     *
     * @param userId   the user ID associated with the token
     * @param userType the type of the user (e.g., "ROLE_ADMIN", "ROLE_PLAYER")
     * @param tokenType the type of the token (e.g., EMAIL_VERIFICATION)
     * @param isUsed   the used status of the token (true if the token has been used, false if not)
     * @return an Optional containing the found UserToken, or empty if no token matches
     */
    Optional<UserToken> findByUserIdAndUserTypeAndTokenTypeAndUsed(
            Long userId,
            String userType,
            UserToken.TokenType tokenType,
            boolean isUsed
    );

    /**
     * Finds a user token by token value and token type.
     *
     * @param token     the unique token string
     * @param tokenType the type of the token (e.g., EMAIL_VERIFICATION)
     * @return an Optional containing the found UserToken, or empty if no token matches
     */
    Optional<UserToken> findByTokenAndTokenType(String token, UserToken.TokenType tokenType);

    /**
     * Finds a user token by user ID, token type, and the 'used' status.
     *
     * @param userId   the user ID associated with the token
     * @param tokenType the type of the token (e.g., EMAIL_VERIFICATION)
     * @param used     the used status of the token (true if the token has been used, false if not)
     * @return an Optional containing the found UserToken, or empty if no token matches
     */
    Optional<UserToken> findByUserIdAndTokenTypeAndUsed(Long userId, UserToken.TokenType tokenType, boolean used);
}
