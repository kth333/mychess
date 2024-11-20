package com.g1.mychess.auth.service;

import com.g1.mychess.auth.exception.UserTokenException;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class TokenServiceTest {
    @Mock
    UserTokenRepository userTokenRepository;
    @InjectMocks
    TokenService tokenService;

    @BeforeEach
    public void setUp() {
        // No set up needed
    }

    /**
     * If token cannot be found in the UserTokenRepository it must be invalid.
     */
    @Test
    void validateToken_InvalidToken() {
        String token ="INVALID_TOKEN" ;
        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
        UserToken userToken = mock(UserToken.class);
        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.empty());

        Exception expectedException = assertThrows(UserTokenException.class, () ->
                tokenService.validateToken(token, tokenType));
        assertEquals("Token is invalid.", expectedException.getMessage());

    }

    /**
     * Token has been used before and should not be used again.
     */

    @Test
    void validateToken_TokenWasUsed(){
        String token ="USED_TOKEN" ;
        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
        UserToken userToken = mock(UserToken.class);

        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(Boolean.TRUE);

        Exception expectedException = assertThrows(UserTokenException.class, () ->
                tokenService.validateToken(token, tokenType));
        assertEquals("Token already used.", expectedException.getMessage());
    }

    /**
     * Token expiration is in the past, so the token should be considered expired
     */
    @Test
    void validateToken_ExpiredToken(){
        String token ="EXPIRED_TOKEN" ;
        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
        UserToken userToken = mock(UserToken.class);
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);

        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(false);
        when(userToken.getExpirationTime()).thenReturn(expiredTime);

        Exception expectedException = assertThrows(UserTokenException.class, () ->
                tokenService.validateToken(token, tokenType));
        assertEquals("Token has expired.", expectedException.getMessage());
    }



}
