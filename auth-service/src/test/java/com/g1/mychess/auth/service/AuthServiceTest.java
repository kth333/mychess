package com.g1.mychess.auth.service;

import com.g1.mychess.auth.client.AdminServiceClient;
import com.g1.mychess.auth.client.EmailServiceClient;
import com.g1.mychess.auth.client.PlayerServiceClient;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.UserTokenException;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.util.UserDetailsFactory;


import com.g1.mychess.auth.service.impl.AuthServiceImpl;
import com.g1.mychess.auth.util.JwtUtil;

import com.g1.mychess.player.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTest {

    @Value("${player.service.url}")
    private String playerServiceUrl;

    @Value("${admin.service.url}")
    private String adminServiceUrl;

    @Value("${email.service.url}")
    private String emailServiceUrl;

    private static final Logger log = LoggerFactory.getLogger(AuthServiceTest.class);
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
    @MockBean
    TokenService tokenService;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private UserTokenRepository userTokenRepository;

    @Mock
    private UserDetailsFactory userDetailsFactory;

    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    /*      << Methods that should be tested >>
     * registerUser(RegisterRequestDTO registerRequestDTO)       left with sucess case.
     * login(String username, String password, String role)      done
     * resetPassword(String resetToken, String newPassword)      done
     * verifyEmail(String token)                                 done
     * isEmailVerified(Long userId, String userType)             done
     * isValidPassword(String password)                          done
     * isValidEmail(String email)                                done
     */

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
//
//        authServiceImpl = new AuthServiceImpl(passwordEncoder,jwtUtil,playerServiceClient,adminServiceClient,emailServiceClient,tokenService);

        webClient = mock(WebClient.class);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        requestBodySpec = mock(WebClient.RequestBodySpec.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
    }

    @Test
    public void resetPassword_NewPassword_Is_Invalid_should_throw_Exception() {
        String resetToken = "validToken";
        String newPassword = "badPassword";

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Password does not meet the requirements.", expectedThrow.getMessage());
    }

    @Test
    public void resetPassword_used_should_throw_Exception() {
        String resetToken = "badToken";
        String newPassword = "NewPassword123";
        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
        UserToken token = mock(UserToken.class);

        when(tokenService.validateToken(resetToken, tokenType)).thenReturn(token);
        when(userTokenRepository.findByTokenAndTokenType(resetToken, tokenType)).thenReturn(Optional.empty());

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Token is invalid.", expectedThrow.getMessage());
    }

    @Test
    public void resetPassword_usedToken_should_throw_Exception() {
        String resetToken = "usedToken";
        String newPassword = "newPassword123";

        UserToken userToken = mock(UserToken.class);
        when(userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)).thenReturn(Optional.ofNullable(userToken));
        when(userToken.isUsed()).thenReturn(true);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Reset token already used.", expectedThrow.getMessage());
    }

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

    @Test
    public void resetPassword_Success() {
        String resetToken = "resetToken";
        String newPassword = "newPassword";
        Long userID = 1L;
        String encodedPassword = "encodedPassword";
        UserDTO userDTO = mock(UserDTO.class);

        LocalDateTime expiredTime = LocalDateTime.now().plusHours(1);

        UserToken userToken = mock(UserToken.class);
        when(authServiceImpl.isValidPassword(newPassword)).thenReturn(true);
        when(userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)).thenReturn(Optional.ofNullable(userToken));
        when(userToken.isUsed()).thenReturn(false);
        when(userToken.getExpirationTime()).thenReturn(expiredTime);

        // stubs for fetchPlayerFromPlayerServiceById()
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/playerId/" + userID)).thenReturn(requestBodySpec);
        when(requestBodySpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

        when(userToken.getUserId()).thenReturn(userID);

        UpdatePasswordRequestDTO updatePasswordRequestDTO = new UpdatePasswordRequestDTO(userID, encodedPassword);
        // stubs for updatePasswordInPlayerService()
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(playerServiceUrl + "/api/v1/player/update-password")).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(updatePasswordRequestDTO)).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        ResponseEntity<String> response = authServiceImpl.resetPassword(resetToken, newPassword);

        verify(userTokenRepository).save(userToken); // Ensure the token is marked as used
        verify(passwordEncoder).encode(newPassword); // Ensure the password encoding is called
        assertEquals("Password has been reset successfully.", response.getBody());
    }


    @Test
    void isValidPassword_GoodPassword() {
        // Arrange
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "VeryStrong1337", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        // Act
        boolean actualResult = authServiceImpl.isValidPassword(goodPasswordRequestDTO.getPassword());

        // Assert
        assertTrue(actualResult);
    }

    @Test
    void isValidPassword_TooShortPassword() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO shortPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "Pass01", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidPassword(shortPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidPassword_PasswordWithoutNumbers() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO noNumberPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "PasswordWithoutNumber", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidPassword(noNumberPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_GoodEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(goodEmailRequestDTO.getEmail());

        assertTrue(actualResult);
    }

    @Test
    void isValidEmail_BadLocalPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badLocalPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Bad!@#$%^&*()_email@example.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(badLocalPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    @Test
    void isValidEmail_BadDomainPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badDomainPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Invalid.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(badDomainPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

//    @Test
//    void testIsEmailVerified_success() {
//        UserToken userToken = mock(UserToken.class);
//        long userId = 1L;
//        String userType = "ROLE_PLAYER";
//        when(userTokenRepository.findByUserIdAndUserTypeAndTokenTypeAndUsed(
//                userId,
//                userType,
//                UserToken.TokenType.EMAIL_VERIFICATION,
//                true
//        )).thenReturn(Optional.of(userToken));
//
//        boolean result = authServiceImpl.isEmailVerified(userId, userType);
//
//        assertTrue(result);
//    }

    @Test
    void testIsEmailVerified_fail() {

        UserToken userToken = mock(UserToken.class);
        Long userID = 1L;
        String userType = "ROLE_PLAYER";

        when(userTokenRepository.findByUserIdAndUserTypeAndTokenTypeAndUsed(
                userID,
                userType,
                UserToken.TokenType.EMAIL_VERIFICATION,
                false
        )).thenReturn(Optional.empty());

        boolean result = authServiceImpl.isEmailVerified(userID, userType);

        assertFalse(result);
    }

    @Test
    void registerUserBadPassword_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "bad", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.registerUser(badPasswordRequestDTO));


    }

    @Test
    void registerUserBadEmail_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "BAD@(!_EMAIL.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.registerUser(badEmailRequestDTO));

    }

//    @Test
//    void registerUser_Success()throws Exception{
//        LocalDate localDate = LocalDate.now().minusYears(20);
//        String encodedPassword = "EncodedPassword";
//        RegisterRequestDTO requestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);
//        // stub for password check
//        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn(encodedPassword);
//
//        ResponseEntity<PlayerCreationResponseDTO> responseEntity = mock(ResponseEntity.class);
//
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(playerServiceUrl + "/api/v1/player/create")).thenReturn(requestBodySpec);
//        when(requestBodySpec.bodyValue(requestDTO)).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        PlayerCreationResponseDTO playerCreationResponseDTO = mock(PlayerCreationResponseDTO.class);
////        when(responseSpec.toEntity(PlayerCreationResponseDTO.class)).thenReturn(playerCreationResponseDTO);
//        when(responseSpec.toEntity(PlayerCreationResponseDTO.class)).thenReturn(Mono.just(responseEntity));
//
//        when(responseEntity.getStatusCode().is2xxSuccessful()).thenReturn(true);
//        when(responseEntity.getBody()).thenReturn(playerCreationResponseDTO);
//        when(playerCreationResponseDTO != null).thenReturn(true);
//        when(playerCreationResponseDTO.getPlayerId()!=null).thenReturn(true);

//        when(authServiceImpl.generateToken(responseBody.getPlayerId(), "ROLE_PLAYER", UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));)
    // Stub sendVerificationEmail methods
    //

//    }


    @Test
    void player_Login_Success() throws Exception {

    }

    @Test
    void Player_Login_Wrong_Credentials_Should_Throw_Exception() throws Exception {
        String username = "Test-User";
        String password = "Password123";
        String role = "ROLE_PLAYER";

        UserDTO userDTO = mock(UserDTO.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(playerServiceUrl + "/api/v1/player/username/" + username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

        when(playerServiceClient.fetchPlayerByUsername(username)).thenReturn(userDTO);
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(false);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.login(username, password, role));
        assertEquals("Invalid username or password.", expectedThrow.getMessage());
    }

    @Test
    void Player_Login_UnverifiedEmail_Should_Throw_Exception() throws Exception {

    }


    /**
     * Test is broken.
     * Error on the line when(jwtUtil.generateToken(userDetails, userId)).thenReturn(mockedToken);
     * even though the method signature matches jwtUtil.generateToken
     * @throws Exception
     */
//    @Test
//    void Admin_Login_Success() throws Exception {
//        String username = "ValidUser";
//        String password = "Password123";
//        String role = "ROLE_ADMIN";
//        String mockedToken = "mockedToken";
//
//        UserDTO userDTO = mock(UserDTO.class);
//        String encodedPassword = "EncodedPassword";
//        // stub for fetch
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(adminServiceUrl + "/api/v1/admin/username/" + username)).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));
//
//        when(userDTO.getPassword()).thenReturn(encodedPassword); // Return encoded password from UserDTO
//        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true); // Match raw password with encoded password
//
//        long userId = 1L;
//        when(userDTO.getUserId()).thenReturn(userId);
//        when(authServiceImpl.isEmailVerified(userId, role)).thenReturn(true);
//
//        UserDetails userDetails = mock(UserDetails.class);
//        when(userDTO.getPassword()).thenReturn(encodedPassword);
//        when(userDTO.getUsername()).thenReturn(username);
//        when(jwtUtil.generateToken(userDetails, userId)).thenReturn(mockedToken);
//
//        String result = authServiceImpl.login(username, password, role);
//
//        verify(passwordEncoder).matches(password, userDTO.getPassword());
//        verify(authServiceImpl).isEmailVerified(userId, role);
//    }

    @Test
    void Admin_Login_Wrong_Credentials_Should_Throw_Exception() throws Exception {
        String username = "";
        String password = "";
        String role = "ROLE_ADMIN";

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.login(username, password, role));

    }

    @Test
    void Admin_Login_UnverifiedEmail_Should_Throw_Exception() throws Exception {
        String username = "ValidUser";
        String password = "Password123";
        String role = "ROLE_ADMIN";

        UserDTO userDTO = mock(UserDTO.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(adminServiceUrl + "/api/v1/admin/username/" + username)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));

//        when(authServiceImpl.fetchAdminFromAdminService(username)).thenReturn(userDTO);
        when(passwordEncoder.matches(password, userDTO.getPassword())).thenReturn(true);

        long userId = 1L;
        when(userDTO.getUserId()).thenReturn(userId);
        when(authServiceImpl.isEmailVerified(userId, role)).thenReturn(false);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.login(username, password, role));

//        verify(authServiceImpl).fetchAdminFromAdminService(username);
        verify(passwordEncoder).matches(password, userDTO.getPassword());
        verify(authServiceImpl).isEmailVerified(userId, role);
    }

//    @Test
//    void testVerifyEmail_alreadyUsedToken() {
//        String token ="INVALID_TOKEN" ;
//        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
//        UserToken userToken = mock(UserToken.class);
//
//        when(tokenService.validateToken(token, UserToken.TokenType.EMAIL_VERIFICATION)).thenReturn(userToken);
//        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.empty());
//        when(userToken.isUsed()).thenReturn(Boolean.TRUE);
//
//        UserTokenException exception = assertThrows(UserTokenException.class,
//                () -> authServiceImpl.verifyEmail(token));
//        assertEquals("Your email is already verified. You can proceed to log in.", exception.getMessage());
//    }

    @Test
    void testVerifyEmail_expiredToken() {
        String token ="EXPIRED_TOKEN" ;
        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
        UserToken userToken = mock(UserToken.class);
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);

        when(userTokenRepository.findByTokenAndTokenType(token,tokenType)).thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(false);
        when(userToken.getExpirationTime()).thenReturn(expiredTime);

        Exception expectedException = assertThrows(UserTokenException.class, () ->
                authServiceImpl.verifyEmail(token));
        assertEquals("Token has expired.", expectedException.getMessage());
    }

    @Test
    void testVerifyEmail_successfulVerification() {
        String token = "validToken";
        UserToken userToken = mock(UserToken.class);
        LocalDateTime tokenExpirationTime =  LocalDateTime.now().plusDays(1); // Token is still valid

        when(tokenService.validateToken(token, UserToken.TokenType.EMAIL_VERIFICATION)).thenReturn(userToken);
        when(userTokenRepository.findByTokenAndTokenType(token, UserToken.TokenType.EMAIL_VERIFICATION))
                .thenReturn(Optional.of(userToken));
        when(userToken.isUsed()).thenReturn(Boolean.FALSE);
        when(userToken.getExpirationTime()).thenReturn(tokenExpirationTime);

        authServiceImpl.verifyEmail(token);

        assertTrue(userToken.isUsed());
        verify(userTokenRepository).save(userToken); // Ensure that the token was saved with 'used' set to true
    }

//
//    @Test
//    void requestPasswordReset_Success() {
//        long userId = 1L;
//        String username = "username";
//        String email = "email@domain.com";
//        String role = "ROLE_PLAYER";
//        String verificationToken = "VerificationToken";
//        UserDTO userDTO = mock(UserDTO.class);
//        UserToken userToken = mock(UserToken.class);
//        UserToken.TokenType tokenType = mock(UserToken.TokenType.class);
//
//        LocalDateTime expirationTime = LocalDateTime.now().plusDays(1);
//
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.get()).thenReturn(requestHeadersUriSpec);
//        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(UserDTO.class)).thenReturn(Mono.just(userDTO));
//
//        when(userDTO == null).thenReturn(false);
//        when(userDTO.getUserId()).thenReturn(1L);
//        when(userDTO.getRole()).thenReturn(role);
//        when(authServiceImpl.isEmailVerified(userId, role)).thenReturn(false);
//        // generateToken(userId, userDTO.getRole(), UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));
//        when(UserToken.TokenType.EMAIL_VERIFICATION).thenReturn(mock(UserToken.TokenType.class));
//        when(userTokenRepository.findByUserIdAndUserTypeAndTokenType(userId, role, tokenType)).thenReturn(Optional.of(userToken));
//
//        // Create the EmailRequestDTO object
//        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
//        emailRequestDTO.setTo(email);
//        emailRequestDTO.setUsername(username);
//        emailRequestDTO.setUserToken(verificationToken);
//
//        // stub for sendVerificationEmail();
//        when(webClientBuilder.build()).thenReturn(webClient);
//        when(webClient.post()).thenReturn(requestBodyUriSpec);
//        when(requestBodyUriSpec.uri(emailServiceUrl + "/api/v1/email/send-verification")).thenReturn(requestBodySpec);
//        when(requestBodySpec.bodyValue(emailRequestDTO)).thenReturn(requestHeadersSpec);
//        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
//        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("Email sent successfully!"));
//
//        ResponseEntity<String> result = authServiceImpl.requestPasswordReset(email);
//
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//    }

}



