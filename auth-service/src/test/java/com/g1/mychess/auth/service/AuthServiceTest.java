package com.g1.mychess.auth.service;

import com.g1.mychess.auth.config.SecurityTestConfig;
import org.springframework.context.annotation.Import;


import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.dto.UserDTO;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.service.impl.AuthServiceImpl;
import com.g1.mychess.auth.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import({SecurityTestConfig.class})
public class AuthServiceTest {

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${admin.service.url}")
    private String adminServiceUrl;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    @InjectMocks
    private AuthServiceImpl authService;  // Use AuthServiceImpl instead of AuthService

    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserTokenRepository verificationTokenRepository;
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
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

        
    }

    @Test
    void registerUserBadEmail_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "BAD@(!_EMAIL.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authService.registerUser(badEmailRequestDTO));
        
    }

    @Test
    void Player_Login_Success()throws Exception{
        String username = "ValidUser";
        String password = "Password123";
        String role = "ROLE_PLAYER";

        UserDTO userDTO = mock(UserDTO.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/username/" + username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

        when(authService.fetchPlayerFromPlayerService(username)).thenReturn(userDTO);
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(true);

        long userId = 1L;
        when(userDTO.getUserId()).thenReturn(userId);
        when(authService.isEmailVerified(userId, role)).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDTO.getUsername()).thenReturn(username);
        when(userDTO.getPassword()).thenReturn(password);

        String result = authService.login(username, password, role);
        assertNotNull(result);

        verify(authService).fetchPlayerFromPlayerService(username);
        verify(passwordEncoder).matches(password, userDTO.getPassword());
        verify(authService).isEmailVerified(userId, role);

    }

    @Test
    void Player_Login_Wrong_Credentials_Should_Throw_Exception()throws Exception{
        String username = "";
        String password = "";
        String role = "ROLE_PLAYER";

        UserDTO userDTO = mock(UserDTO.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/username/" + username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

        when(authService.fetchPlayerFromPlayerService(username)).thenReturn(userDTO);
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(false);

        Exception expectedThrow = assertThrows(Exception.class,
                ()->authService.login(username,password,role));

        verify(authService).fetchPlayerFromPlayerService(username);
        verify(passwordEncoder).matches(password, userDTO.getPassword());
    }

    @Test
    void Player_Login_UnverifiedEmail_Should_Throw_Exception()throws Exception{
        String username = "ValidUser";
        String password = "Password123";
        String role = "ROLE_PLAYER";

        UserDTO userDTO = mock(UserDTO.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/username/" + username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

        when(authService.fetchPlayerFromPlayerService(username)).thenReturn(userDTO);
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(true);

        long userId = 1L;
        when(userDTO.getUserId()).thenReturn(userId);
        when(authService.isEmailVerified(userId, role)).thenReturn(false);

        Exception expectedThrow = assertThrows(Exception.class,
                ()->authService.login(username,password,role));

        verify(authService).fetchPlayerFromPlayerService(username);
        verify(passwordEncoder).matches(password, userDTO.getPassword());
        verify(authService).isEmailVerified(userId, role);
    }

//    @Test
//    void Player_Login_Null_UserDTO_should_Throw_Exception()throws Exception{
//        String username = "ValidUser";
//        String password = "Password123";
//        String role = "ROLE_PLAYER";
//
//        UserDTO userDTO = mock(UserDTO.class);
//
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/username/" + username)).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        /* Mono cannot return null */
//        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(null));
//
//        Exception expectedThrow = assertThrows(Exception.class,
//                ()->authService.login(username,password,role));
//
//        verify(authService).fetchPlayerFromPlayerService(username);
//        verify(passwordEncoder,never()).matches(password, userDTO.getPassword());
//    }


    // ToDO:
    /* Login Method exit points (for Player and for Admin)
    1 UserDTO null
    2 PasswordEncoderMatches
    3 isEmailVerified(userId, role)
    4 Success
     */

    // GenerateToken
    // verifyEmail
    // isEmailVerified

    // Anything containing Reactor WebClient method should be integration Tested

}

