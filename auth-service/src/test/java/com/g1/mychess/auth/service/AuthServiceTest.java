package com.g1.mychess.auth.service;

import com.g1.mychess.auth.client.AdminServiceClient;
import com.g1.mychess.auth.client.EmailServiceClient;
import com.g1.mychess.auth.client.PlayerServiceClient;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.service.impl.AuthServiceImpl;
import com.g1.mychess.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**

 This class contains unit tests for the `AuthServiceImpl` class.
 <p>
 It uses the Mockito framework to mock dependencies and the JUnit framework for testing.
 The tests cover scenarios for password validation, email validation, and password resetting.
 */

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the scenario where resetting the password with an invalid new password should throw an exception.
     * <p>
     * This test validates that the `resetPassword` method in `authServiceImpl` throws the correct exception
     * when the provided new password does not meet the security requirements. It asserts that the exception
     * message matches the expected invalid password message.
     * <p>
     * Preconditions:
     * - A valid reset token is provided.
     * - The new password provided does not meet the security requirements.
     * <p>
     * Expected behavior:
     * - The `resetPassword` method throws an `Exception` with the message "Password does not meet the requirements."
     *
     * @throws Exception if the password reset fails due to an invalid password.
     */
    @Test
    public void resetPassword_NewPassword_Is_Invalid_should_throw_Exception() {
        String resetToken = "validToken";
        String newPassword = "badPassword";

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.resetPassword(resetToken, newPassword));
        assertEquals("Password does not meet the requirements.", expectedThrow.getMessage());
    }

    /**
     * Tests that the `isValidPassword` method in `authServiceImpl` correctly validates a strong password.
     * <p>
     * This test is designed to check the behavior of the `isValidPassword` method when provided with a password that
     * meets all security requirements. It ensures that such a password is recognized as valid.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with a strong password is created and provided.
     * <p>
     * Expected behavior:
     * - The `isValidPassword` method returns `true` indicating the password is valid.
     * <p>
     * Steps:
     * 1. Create a `RegisterRequestDTO` object with a strong password (at least 8 characters long, contains numbers).
     * 2. Call the `isValidPassword` method with the password from the DTO.
     * 3. Assert that the method returns `true`.
     */
    @Test
    void isValidPassword_Successful_StrongPassword() {
        // Arrange
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "VeryStrong1337", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        // Act
        boolean actualResult = authServiceImpl.isValidPassword(goodPasswordRequestDTO.getPassword());

        // Assert
        assertTrue(actualResult);
    }

    /**
     * Tests that the `isValidPassword` method in `authServiceImpl` correctly identifies a password as invalid if it is too short.
     * <p>
     * This test checks the behavior of the `isValidPassword` method when provided with a password that does not meet the minimum
     * length requirement. It ensures that such a password is recognized as invalid.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with a short password is created and provided.
     * <p>
     * Expected behavior:
     * - The `isValidPassword` method returns `false`, indicating the password is invalid.
     * <p>
     * Steps:
     * - Create a `RegisterRequestDTO` object with a password that is less than 8 characters long.
     * - Call the `isValidPassword` method with the password from the DTO.
     * - Assert that the method returns `false`.
     */
    @Test
    void isValidPassword_TooShortPassword() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO shortPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "Pass01", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidPassword(shortPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    /**
     * Tests the `isValidPassword` method of `authServiceImpl` for a password without numeric characters.
     * <p>
     * This test validates that the `isValidPassword` method correctly identifies a password as
     * invalid when it does not contain any numeric digits.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with a password that lacks numeric characters is created.
     * <p>
     * Expected behavior:
     * - The `isValidPassword` method returns `false`, indicating the password is invalid.
     * <p>
     * Steps:
     * 1. Create a `RegisterRequestDTO` object with a password that contains only letters, no numbers.
     * 2. Call the `isValidPassword` method with the password from the DTO.
     * 3. Assert that the method returns `false`.
     */
    @Test
    void isValidPassword_PasswordWithoutNumbers() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO noNumberPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "PasswordWithoutNumber", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidPassword(noNumberPasswordRequestDTO.getPassword());

        assertFalse(actualResult);
    }

    /**
     * Tests the `isValidEmail` method for a valid email address.
     * <p>
     * This test validates that the `isValidEmail` method in `authServiceImpl` correctly
     * identifies an email address as valid when it meets all required criteria.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with a valid email address is created.
     * <p>
     * Expected behavior:
     * - The `isValidEmail` method returns `true`, indicating the email is valid.
     * <p>
     * Steps:
     * 1
     */
    @Test
    void isValidEmail_GoodEmail_Success() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO goodEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(goodEmailRequestDTO.getEmail());

        assertTrue(actualResult);
    }

    /**
     * Tests the `isValidEmail` method for an email address with an invalid local part.
     * <p>
     * This test validates that the `isValidEmail` method in `authServiceImpl` correctly
     * identifies an email address as invalid when its local part contains unacceptable characters.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with an email address that has a bad local part is created.
     * <p>
     * Expected behavior:
     * - The `isValidEmail` method returns `false`, indicating the email is invalid.
     * <p>
     * Steps:
     * 1. Create a `RegisterRequestDTO` object with an email that has an invalid local part (e.g., containing special characters).
     * 2. Call the `isValidEmail` method with the email from the DTO.
     * 3. Assert that the method returns `false`.
     */
    @Test
    void isValidEmail_BadLocalPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badLocalPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Bad!@#$%^&*()_email@example.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(badLocalPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    /**
     * Tests the `isValidEmail` method for an email address with an invalid domain part.
     * <p>
     * This test validates that the `isValidEmail` method in `authServiceImpl` correctly
     * identifies an email address as invalid when its domain part contains errors or
     * unacceptable format.
     * <p>
     * Preconditions:
     * - A `RegisterRequestDTO` instance with an email address that has a bad domain part is created.
     * <p>
     * Expected behavior:
     * - The `isValidEmail` method returns
     */
    @Test
    void isValidEmail_BadDomainPartEmail() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badDomainPartEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "Invalid.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        boolean actualResult = authServiceImpl.isValidEmail(badDomainPartEmailRequestDTO.getEmail());

        assertFalse(actualResult);
    }

    /**
     * Tests the scenario where registering a user with a weak password should throw an exception.
     * <p>
     * This test ensures that the `registerUser` method in `authServiceImpl` throws the appropriate
     * exception when provided with a registration request containing a password that does not meet
     * the required security standards.
     * <p>
     * Preconditions:
     * -
     */
    @Test
    void registerUserBadPassword_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badPasswordRequestDTO = new RegisterRequestDTO("ValidUsername", "bad", "ValidEmail@domain.com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.registerUser(badPasswordRequestDTO));

    }

    /**
     * Tests the scenario where registering a user with an invalid email should throw an exception.
     *
     * This test ensures that the `registerUser` method in `authServiceImpl` throws an appropriate
     * exception when provided with a registration request containing an improperly formatted email address.
     *
     * Preconditions:
     * - A `RegisterRequestDTO` instance with an invalid email format is created, specifically containing disallowed characters.
     *
     * Expected behavior:
     * - The `registerUser` method throws an `Exception`, indicating the email format is invalid.
     *
     * @throws Exception if the method call to `authServiceImpl.registerUser` results in an error.
     */
    @Test
    void registerUserBadEmail_Should_ThrowException() {
        LocalDate localDate = LocalDate.now().minusYears(20);
        RegisterRequestDTO badEmailRequestDTO = new RegisterRequestDTO("ValidUsername", "Password123", "BAD@(!_EMAIL.domain@com", "Male", "Durkadurkastan", "Capital", "Bakalakadaka", localDate);

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.registerUser(badEmailRequestDTO));

    }

    /**
     * Tests the scenario where an administrator attempts to log in with incorrect credentials.
     *
     * This test validates that the `login` method in `authServiceImpl` throws the correct exception
     * when provided with an incorrect username, password, or role. It ensures that invalid login attempts
     * are properly handled and that an appropriate exception is thrown.
     *
     * Preconditions:
     * - The username, password, and role do not match any existing valid combination of credentials.
     *
     * Expected behavior:
     * - The `login` method throws an `Exception` indicating invalid credentials.
     *
     * @throws Exception if the login attempt is invalid due to incorrect credentials.
     */
    @Test
    void Admin_Login_Wrong_Credentials_Should_Throw_Exception() throws Exception {
        String username = "";
        String password = "";
        String role = "ROLE_ADMIN";

        Exception expectedThrow = assertThrows(Exception.class,
                () -> authServiceImpl.login(username, password, role));

    }


}



