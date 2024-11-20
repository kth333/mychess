package com.g1.mychess.auth.service;

import com.g1.mychess.auth.client.AdminServiceClient;
import com.g1.mychess.auth.client.EmailServiceClient;
import com.g1.mychess.auth.client.PlayerServiceClient;
import com.g1.mychess.auth.exception.UserTokenException;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.service.impl.AuthServiceImpl;
import com.g1.mychess.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Integration tests for the AuthService focusing on token-related operations.
 */

@ExtendWith(MockitoExtension.class)
public class AuthServiceTokenServiceIntegrationTest {
    @MockBean
    PasswordEncoder passwordEncoder;
    @MockBean
    JwtUtil jwtUtil;
    @MockBean
    PlayerServiceClient playerServiceClient;
    @MockBean
    AdminServiceClient adminServiceClient;
    @MockBean
    EmailServiceClient emailServiceClient;

    TokenService tokenService;

    private AuthServiceImpl authServiceImpl;

    @Mock
    private UserTokenRepository userTokenRepository;

    /**
     * Sets up the test environment for each test method in the AuthServiceTest2 class.
     *
     * This method performs the following initializations before each test:
     * 1. Opens mocks for the test class using MockitoAnnotations.
     * 2. Initializes a real instance of TokenService with the mocked UserTokenRepository.
     * 3. Initializes the AuthServiceImpl with the necessary dependencies including
     *    passwordEncoder, jwtUtil, playerServiceClient, adminServiceClient, emailServiceClient, and tokenService.
     */

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenService(userTokenRepository);
        authServiceImpl = new AuthServiceImpl(passwordEncoder,jwtUtil,playerServiceClient,adminServiceClient,emailServiceClient,tokenService);
    }

    /**
     * Test to verify the behavior when attempting to verify an email with an expired token.
     *
     * This test ensures that the AuthServiceImpl correctly throws a {@link UserTokenException}
     * when the provided token is expired.
     *
     * Preconditions:
     * - The token used for email verification is expired.
     * - The token is not marked as used.
     *
     * Expected Outcome:
     * - The method should throw a {@link UserTokenException} with the message "Token has expired."
     *
     * @throws Exception if an unexpected error occurs during the test execution
     */

    @Test
    void testVerifyEmail_expiredToken() throws Exception {
        String token ="EXPIRED_TOKEN" ;
        UserToken.TokenType tokenType = UserToken.TokenType.EMAIL_VERIFICATION;
        UserToken userToken = mock(UserToken.class);
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);

        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(false);
        when(userToken.getExpirationTime()).thenReturn(expiredTime);

        Exception expectedException = assertThrows(UserTokenException.class, () ->
                authServiceImpl.verifyEmail(token));
        assertEquals("Token has expired.", expectedException.getMessage());
    }

    /**
     * Test to verify the behavior when attempting to reset a password using a token that has already been used.
     *
     * This test ensures that the AuthServiceImpl correctly throws a {@link UserTokenException}
     * when the provided token is marked as used.
     *
     * Preconditions:
     * - The reset token has been used.
     *
     * Expected Outcome:
     * - The method should throw a {@link UserTokenException} with the message "Token already used."
     *
     * @throws Exception if an unexpected error occurs during the test execution
     */
    @Test
    public void resetPassword_usedToken_should_throw_Exception() throws Exception {
        String resetToken = "usedToken";
        String newPassword = "newPassword123";

        UserToken userToken = mock(UserToken.class);
        when(userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)).thenReturn(Optional.ofNullable(userToken));
        when(userToken.isUsed()).thenReturn(true);

        Exception expectedThrow = assertThrows(UserTokenException.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Token already used.", expectedThrow.getMessage());
    }

    /**
     * Test to verify the behavior when attempting to reset a password using an invalid reset token.
     *
     * This test ensures that the AuthServiceImpl correctly throws a {@link UserTokenException}
     * when the provided reset token does not match any existing token in the repository.
     *
     * Preconditions:
     * - The reset token used is not found in the repository.
     *
     * Expected Outcome:
     * - The method should throw a {@link UserTokenException} with the message "Token is invalid."
     *
     * @throws Exception if an unexpected error occurs during the test execution
     */
    @Test
    public void resetPassword_used_should_throw_Exception() throws Exception {
        String resetToken = "badToken";
        String newPassword = "NewPassword123";
        UserToken.TokenType tokenType = UserToken.TokenType.PASSWORD_RESET;
        UserToken token = mock(UserToken.class);

        when(userTokenRepository.findByTokenAndTokenType(resetToken, tokenType)).thenReturn(Optional.empty());
        Exception expectedThrow = assertThrows(UserTokenException.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Token is invalid.", expectedThrow.getMessage());
    }

    /**
     * Test to verify the behavior when attempting to reset a password with an expired reset token.
     *
     * This test ensures that the AuthServiceImpl correctly throws an {@link Exception} when the provided token is expired.
     *
     * Preconditions:
     * - The reset token is expired.
     * - The token is not marked as used.
     *
     * Expected Outcome:
     * - The method should throw an {@link Exception} with the message "Token has expired."
     */
    @Test
    public void resetPassword_expiredToken_should_throw_Exception() {
        String resetToken = "expiredToken";
        String newPassword = "newPassword123";
        long userId = 1L;
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1); // expired

        UserToken userToken = mock(UserToken.class);
        when(userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)).thenReturn(Optional.ofNullable(userToken));
        when(userToken.isUsed()).thenReturn(false);
        when(userToken.getExpirationTime()).thenReturn(expiredTime);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Token has expired.", expectedThrow.getMessage());
    }

    /**
     * Test to verify the behavior when attempting to verify an email with a token that has already been used.
     *
     * This test ensures that the AuthServiceImpl correctly throws a {@link UserTokenException}
     * when the provided token is marked as used.
     *
     * Preconditions:
     * - The token used for email verification has already been used.
     *
     * Expected Outcome:
     * - The method should throw a {@link UserTokenException} with the message "Token already used."
     *
     * @*/
    @Test
    void testVerifyEmail_alreadyUsedToken() {
        String token ="INVALID_TOKEN" ;
        UserToken.TokenType tokenType = UserToken.TokenType.EMAIL_VERIFICATION;
        UserToken userToken = mock(UserToken.class);

        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(Boolean.TRUE);

        UserTokenException expectedException = assertThrows(UserTokenException.class,
                () -> authServiceImpl.verifyEmail(token));
        assertEquals("Token already used.", expectedException.getMessage());
    }

    /**
     * Test to verify the successful scenario of email verification.
     *
     * This test mocks a UserToken and sets up the behavior of the userTokenRepository to return
     * an optional containing the mocked token when a valid userId, userType, token type (EMAIL_VERIFICATION),
     * and used status (true) are provided. It then calls the isEmailVerified method on the authServiceImpl
     * and asserts that the returned result is true.
     *
     * Preconditions:
     * - A UserToken with the specified userId, userType, and token type is marked as used.
     *
     * Expected Outcome:
     * - The method should return true, indicating that the email associated with the userId and userType is verified.
     */
    @Test
    void testIsEmailVerified_success() {
        UserToken userToken = mock(UserToken.class);
        long userId = 1L;
        String userType = "ROLE_PLAYER";
        when(userTokenRepository.findByUserIdAndUserTypeAndTokenTypeAndUsed(
                userId,
                userType,
                UserToken.TokenType.EMAIL_VERIFICATION,
                true
        )).thenReturn(Optional.of(userToken));

        boolean result = authServiceImpl.isEmailVerified(userId, userType);

        assertTrue(result);
    }


}
