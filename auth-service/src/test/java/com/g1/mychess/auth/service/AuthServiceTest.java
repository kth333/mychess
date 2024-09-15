package com.g1.mychess.auth.service;


import com.g1.mychess.auth.exception.InvalidPasswordException;
import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.repository.VerificationTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import static com.g1.mychess.auth.service.AuthService.isValidPassword;

public class AuthServiceTest {

    private static AuthService authServiceMock;

    // Mock the dependencies
    @BeforeAll
    static void setUp(){

        WebClient.Builder webClientBuilderMock = mock(WebClient.Builder.class);
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        VerificationTokenRepository verificationTokenRepositoryMock = mock(VerificationTokenRepository.class);
        JwtUtil jwtUtilMock = mock(JwtUtil.class);

        authServiceMock = new AuthService(webClientBuilderMock, passwordEncoderMock, verificationTokenRepositoryMock, jwtUtilMock);
    }

    @Test
    void test_Passwords_Without_Numbers_Should_Throw_InvalidPasswordException() {

        RegisterRequestDTO BadPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "password", "ValidEmail@domain.com");
        InvalidPasswordException expectedThrow = assertThrows(InvalidPasswordException.class, () ->
                authServiceMock.registerUser(BadPasswordregisterRequestDTO),"Expected to throw InvalidPasswordException because password does not contain a number");
        assertTrue(expectedThrow.getMessage().contains("number"));
    }


    @Test
    void test_Password_That_Are_Too_Short_Should_Throw_InvalidPasswordException() {

        RegisterRequestDTO BadPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "P@ssw01", "ValidEmail@domain.com");
        InvalidPasswordException expectedThrow = assertThrows(InvalidPasswordException.class, () ->
                authServiceMock.registerUser(BadPasswordregisterRequestDTO),"Expected to throw InvalidPasswordException because password is too short");
        assertTrue(expectedThrow.getMessage().contains("characters"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "Password01", "P@ssword02", "P@ssw0rd03" })
    void test_isValidPassword_Method(String password) {
        assertTrue(isValidPassword(password));
    }

    @ParameterizedTest
    @CsvSource({
            "password, Should contain a number",
            "!password, Should contain a number",
            "p@sswOrd, Should contain a number",
            "P@ss01, Password length must be at least 8 characters long",
            "Passw0r, Password length must be at least 8 characters long",
            "Passwor, Password length must be at least 8 characters long & one or more numbers"
    })
    void test_isValidPassword_Method_against_Invalid_Passwords(String password, String expectedMessage) {
        assertFalse(isValidPassword(password), expectedMessage);
    }


}