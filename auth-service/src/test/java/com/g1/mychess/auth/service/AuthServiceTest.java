package com.g1.mychess.auth.service;


import com.g1.mychess.auth.dto.PlayerCreationResponseDTO;
import com.g1.mychess.auth.dto.UserDTO;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.dto.RegisterRequestDTO;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.time.LocalDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.g1.mychess.auth.service.AuthService.isValidEmail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import static com.g1.mychess.auth.service.AuthService.isValidPassword;


public class AuthServiceTest {

    @InjectMocks
    private AuthService authServiceMock;
    @Mock
    private WebClient.Builder webClientBuilderMock;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    @Mock
    private WebClient.RequestBodySpec requestBodySpecMock;
    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;



//     Mock the dependencies
//    @BeforeAll
//    static void setUpAll() {
//
//
//    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        webClientBuilderMock = mock(WebClient.Builder.class);
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        UserTokenRepository verificationTokenRepositoryMock = mock(UserTokenRepository.class);
        JwtUtil jwtUtilMock = mock(JwtUtil.class);

        authServiceMock = new AuthService(webClientBuilderMock, passwordEncoderMock, verificationTokenRepositoryMock, jwtUtilMock);

        when(webClientBuilderMock.build()).thenReturn(webClientMock);
//        when(webClientMock.get()).thenReturn(requestBodyUriSpecMock);
    }

    @Test
    void test_Passwords_Without_Numbers_Should_Throw_InvalidPasswordException() {

        RegisterRequestDTO BadPasswordregisterRequestDTO = new RegisterRequestDTO();
        InvalidPasswordException expectedThrow = assertThrows(InvalidPasswordException.class, () ->
                authServiceMock.registerUser(BadPasswordregisterRequestDTO),"Expected to throw InvalidPasswordException because password does not contain a number");
        assertTrue(expectedThrow.getMessage().contains("number"));
    }


    @Test
    void test_Password_That_Are_Too_Short_Should_Throw_InvalidPasswordException() {
        LocalDate localDate = LocalDate.now().plusDays(1);
        RegisterRequestDTO BadPasswordregisterRequestDTO = new RegisterRequestDTO("ValidUsername", "P@ssw01", "ValidEmail@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);
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
    void test_isValidPassword_against_Invalid_Passwords(String password, String expectedMessage) {
        assertFalse(isValidPassword(password), expectedMessage);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "prettyandsimple@example.com",
            "very.common@example.com",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-dash@example.com",
            "Y@example.com"
    })
    void test_isValidEmail_against_Common_Valid_Emails(String emails){
        assertTrue(isValidEmail(emails));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "xing.zhao@163.com",
            "குமார@இலங்கை.org",
            "fādāi.wáng@alibaba.cn",
            "丽华@example.co.cn",
            "mariyam.halim@kampung.my",
            "佐藤.健二@メール.jp",
            "李小龙@wushu.cn",
            "tan_ah_kok@bukit.my",
            "ravi.krishna@chennai.in",
            "muhammad.alim@batu.my",
            "hans.schmidt@example.de",
            "manuela.garcia@correo.co.es",
            "hao-wei123@university.edu.cn",
            "张三_测试@公司邮箱.com",
            "devdas@पुणे.in",
            "ahmad.bin.ismail@melaka.org",
            "jeffrey.o'reilly@example.com",
            "david_steinberg@secure.org",
            "lekha.नागराज@मुंबई.in",
            "trần.quốc.khánh@hanoi.vn"
    })
    void test_isValidEmail_against_Asian_Emails(String emails){
        assertTrue(isValidEmail(emails));
    }

    @ParameterizedTest
    @CsvSource({
   "John.Lim@com, missing top-level domain",
    "AtDotDomain@.example.com, dot at the start of the domain",
    "DoubleDotInDomain1@example..com, double dot in the domain",
    "DoubleDotInDomain2@domain..co.uk, double dot in the domain",
    "bob@website.c@om, multiple @ symbols",
    "DoubleAtSymbol@@example.com, double @ symbol",
    "chloé!$%@example.com, illegal special characters in the local part",
    "张三@公司邮箱.公司, missing top-level domain after dot",
//    "\"name\"@example.com double quotes around the local part, without special escaping",
    "first.last@domain.c_m, underscore in the top-level domain)",
    "@no-local-part.com, missing local part before @",
    "user@domain.-com, dash at the start of the top-level domain",
    "*someone@domain.com, asterisk in the domain",
    "foo@[123.456.789.012], invalid IP address format in the domain",
    "localpart@domain..com, double dots in the domain name",
    "username@example..com, double dots after the domain",
    "NoAtSymbol.com, missing @",
    "username@sub_domain.com, underscore in the domain part, which is not allowed",
    "Why got space@example.com, space in the local part, which is not allowed",
    "name@exam_ple.com, underscore in the domain name"
    })
    void test_isValidEmail_against_Invalid_Emails(String emails, String expectedMessage) {
        assertFalse(isValidEmail(emails),expectedMessage);
    }

    // Logic tested with previous method.
    // no need to repeat by testing this segment too many times.
    @Test
    void test_Invalid_Emails_Should_Throw_InvalidEmailException(){

        LocalDate localDate = LocalDate.now().plusDays(1);
        RegisterRequestDTO badEmailRegisterRequestDTO = new RegisterRequestDTO("ValidUsername", "ValidPassword123", "Bad(),<>:;Local@domain.com","Male","Durkadurkastan","Capital", "Bakalakadaka", localDate);
        InvalidEmailException expectedThrow = assertThrows(InvalidEmailException.class, () ->
                authServiceMock.registerUser(badEmailRegisterRequestDTO),"Expected to throw InvalidEmailException because the local part contains illegal special characters");
        assertTrue(expectedThrow.getMessage().contains("email"));}


    @Test
    void test_VerificationEmail_Throws_UserNotFoundException() {

        EmailSendFailedException expectedThrow = assertThrows(EmailSendFailedException.class, () ->
                authServiceMock.sendVerificationEmail("GoodEmail@domain.com", "ValidUsername", "badToken"));
    }

    // incorrect test. Unable to stub .get(). Leading to NPE
//    @Test
//    void test_resendVerificationEmail_Null_userDTO_Throws_UserNotFoundException(){
//        String testEmail = "testEmail@domain.com";
//        UserDTO userDTOMock = mock(UserDTO.class);
//
//        when(webClientBuilderMock.build()
//                .get()
//                .uri("http://player-service:8081/api/v1/player/email/"+testEmail)
//                .retrieve()
//                .bodyToMono(UserDTO.class)
//                .block()).thenReturn(userDTOMock);
//
//        when(authServiceMock.fetchPlayerFromPlayerServiceByEmail(testEmail)).thenReturn(userDTOMock);
//
//        UserNotFoundException expectedThrow = assertThrows(UserNotFoundException.class, () ->
//                authServiceMock.resendVerificationEmail(testEmail));
//
//    }



//    ------------------------------------------------------------------------------------
//    Mocking webclient is problematic. Use integration test instead. Or Mock WebServer?
//    ------------------------------------------------------------------------------------

//        @Test
//        void testRegisterUser_NullPlayerServiceResponse_ThrowsException() {
//
//            // Make ReponseSpec return null.
//
////            when(requestBodyUriSpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
//            when(requestHeadersSpecMock.retrieve()).thenReturn(null);
//            when(responseSpecMock.toEntity(PlayerCreationResponseDTO.class)).thenReturn(null);
//
//            // Register request DTO
//            RegisterRequestDTO validRegisterRequestDTO = new RegisterRequestDTO("ValidUsername", "ValidPassword123", "valid.email@domain.com");
//
//            // Assert that a PlayerServiceException is thrown due to null response
//            PlayerServiceException expectedThrow = assertThrows(PlayerServiceException.class, () ->
//                    authServiceMock.registerUser(validRegisterRequestDTO), "Expected to throw PlayerServiceException due to null response from player service");
//            assertTrue(expectedThrow.getMessage().contains("No response from player service."));
//        }



//    @Test
//    void test_SendVerificationEmail_4xxResponse_Throws_EmailSendFailedException() {
//
//        // Simulate a 4XX errors
////        when(responseSpecMock.bodyToMono(String.class)).thenThrow(new WebClientResponseException( 404, "Not Found", null, null, null));
//
////        when(requestHeadersSpecMock.retrieve()).thenThrow(new WebClientResponseException(
////                404, "Not Found", null, null, null));
//
//        when(responseSpecMock.bodyToMono(String.class))
//                .thenThrow(new WebClientResponseException(
//                        400, // HTTP Status code
//                        "Bad Request", // Status text
//                        null, // Response headers
//                        null, // Response body
//                        null // Client
//                ));
//
//        // Act & Assert
//        assertThrows(EmailSendFailedException.class, () ->
//                authServiceMock.sendVerificationEmail("GoodEmail@domain.com", "ValidUsername", "verificationToken123"));
//
//        // Verify interactions
//        verify(webClientBuilderMock).build();
//        verify(responseSpecMock).bodyToMono(String.class);
//
//    }
}

