package com.g1.mychess.auth.repository;

import com.g1.mychess.auth.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    Optional<UserToken> findByUserIdAndUserTypeAndTokenType(Long userId, String userType, UserToken.TokenType tokenType);

    Optional<UserToken> findByUserIdAndUserTypeAndTokenTypeAndUsed(
            Long userId,
            String userType,
            UserToken.TokenType tokenType,
            boolean isUsed
    );

    Optional<UserToken> findByTokenAndTokenType(String token, UserToken.TokenType tokenType);

    Optional<UserToken> findByUserIdAndTokenTypeAndUsed(Long userId, UserToken.TokenType tokenType, boolean used);
}
