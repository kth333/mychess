package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceTest.class);
    @InjectMocks
    private AuthService authServiceMock;
    @Mock
    private AuthService authService;
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
        MockitoAnnotations.openMocks(this);

        webClientBuilderMock = mock(WebClient.Builder.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        verificationTokenRepositoryMock = mock(UserTokenRepository.class);
        jwtUtilMock = mock(JwtUtil.class);

        // Spy on authService using a constructor with arguments
        authService = spy(new AuthService(webClientBuilderMock, passwordEncoderMock, verificationTokenRepositoryMock, jwtUtilMock));
    }

    @Test
    void isValidPassword_GoodPassword(){
        // Arrange
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO GoodPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "VeryStrong1337", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        // Act
        boolean actualResult = authService.isValidPassword(GoodPasswordregisterRequestDTO.getPassword());

        //Assert
        assertTrue(actualResult);
    }

    @Test
    void isValidPassword_TooShortPassword(){

        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO shortPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "Pass01", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidPassword(shortPasswordregisterRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidPassword_PasswordWithoutNumbers(){
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO noNumberPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "PasswordWithoutNumber", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidPassword(noNumberPasswordregisterRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_GoodEmail(){
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodEmailregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(goodEmailregisterRequestDTO.getEmail());

        assertTrue(actualResult);
    }

    @Test
    void isValidEmail_BadLocalPartEmail(){
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO BadLocalPartEmailregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Bad!@#$%^&*()_email@example.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(BadLocalPartEmailregisterRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_BadDomainPartEmail(){
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badDomainPartEmailregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Invalid.domain@com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        boolean actualResult = authService.isValidEmail(badDomainPartEmailregisterRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    @Test
    void registerUserBadPassword_Should_ThrowException()throws Exception{
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "bad", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                ()->authService.registerUser(badPasswordRequestDTO));

        verify(authService).registerUser(badPasswordRequestDTO);
    }

    @Test
    void registerUserBadEmail_Should_ThrowException()throws Exception{
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "BAD@(!_EMAIL.domain@com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                ()->authService.registerUser(badEmailRequestDTO));
        verify(authService).registerUser(badEmailRequestDTO);
    }

    // ToDO:
    // Login
    // GenerateToken
    // verifyEmail
    // isEmailVerified

    // Anything containing Reactor WebClient method should be integration Tested

}

