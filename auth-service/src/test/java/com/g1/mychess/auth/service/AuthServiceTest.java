package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.service.impl.AuthServiceImpl;
import com.g1.mychess.auth.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl authService;  // Use AuthServiceImpl instead of AuthService

    @Mock
    private WebClient.Builder webClientBuilderMock;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private UserTokenRepository verificationTokenRepositoryMock;
    @Mock
    private JwtUtil jwtUtilMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
    }

    @Test
    void isValidPassword_GoodPassword() {
        // Arrange
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "VeryStrong1337", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        // Act
        boolean actualResult = authService.isValidPassword(goodPasswordRequestDTO.getPassword());

        // Assert
        assertTrue(actualResult);
    }

    @Test
    void isValidPassword_TooShortPassword() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO shortPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "Pass01", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidPassword(shortPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidPassword_PasswordWithoutNumbers() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO noNumberPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "PasswordWithoutNumber", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidPassword(noNumberPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_GoodEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(goodEmailRequestDTO.getEmail());

        assertTrue(actualResult);
    }

    @Test
    void isValidEmail_BadLocalPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badLocalPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Bad!@#$%^&*()_email@example.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(badLocalPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_BadDomainPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badDomainPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Invalid.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(badDomainPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    @Test
    void registerUserBadPassword_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "bad", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authService.registerUser(badPasswordRequestDTO));

        verify(authService).registerUser(badPasswordRequestDTO);
    }

    @Test
    void registerUserBadEmail_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "BAD@(!_EMAIL.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authService.registerUser(badEmailRequestDTO));
        verify(authService).registerUser(badEmailRequestDTO);
    }

    // ToDO:
    // Login
    // GenerateToken
    // verifyEmail
    // isEmailVerified

    // Anything containing Reactor WebClient method should be integration Tested

}

