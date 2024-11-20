package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for handling authentication and authorization operations,
 * including user registration, login, email verification, and password management.
 */
public interface AuthService {

    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerRequestDTO the DTO containing user registration details
     * @return ResponseEntity containing a success message or error details
     */
    ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO);

    /**
     * Authenticates a user and provides a token for successful login.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param role     the role of the user (e.g., "USER" or "ADMIN")
     * @return a token for accessing authenticated endpoints, or error message if login fails
     */
    String login(String username, String password, String role);

    /**
     * Resends the email verification link to the specified email address.
     *
     * @param email the email address to which the verification email should be sent
     * @return ResponseEntity with a message indicating success or failure of email sending
     */
    ResponseEntity<String> resendVerificationEmail(String email);

    /**
     * Initiates a password reset request by sending a reset link to the user's email.
     *
     * @param email the email address of the user requesting a password reset
     * @return ResponseEntity with a message indicating the result of the reset request
     */
    ResponseEntity<String> requestPasswordReset(String email);

    /**
     * Resets the user's password using a valid reset token.
     *
     * @param resetToken  the token for verifying the password reset request
     * @param newPassword the new password to set for the user
     * @return ResponseEntity with a message indicating the success or failure of the password reset
     */
    ResponseEntity<String> resetPassword(String resetToken, String newPassword);

    /**
     * Verifies a user's email using the provided token.
     *
     * @param token the token sent to the user's email for verification
     */
    void verifyEmail(String token);

    /**
     * Checks if a user's email is verified.
     *
     * @param userId   the ID of the user
     * @param userType the type of user (e.g., "PLAYER" or "ADMIN")
     * @return true if the user's email is verified; false otherwise
     */
    boolean isEmailVerified(Long userId, String userType);
}
