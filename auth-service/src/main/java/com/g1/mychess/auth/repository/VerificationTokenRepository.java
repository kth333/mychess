package com.g1.mychess.auth.repository;

import com.g1.mychess.auth.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByUserIdAndTokenType(Long userId, VerificationToken.TokenType tokenType);

    Optional<VerificationToken> findByTokenAndTokenType(String token, VerificationToken.TokenType tokenType);

    Optional<VerificationToken> findByUserIdAndTokenTypeAndUsed(Long userId, VerificationToken.TokenType tokenType, boolean used);
}
