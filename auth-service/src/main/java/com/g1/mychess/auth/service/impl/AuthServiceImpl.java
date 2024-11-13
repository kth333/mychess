package com.g1.mychess.auth.service.impl;

import com.g1.mychess.auth.client.AdminServiceClient;
import com.g1.mychess.auth.client.EmailServiceClient;
import com.g1.mychess.auth.client.PlayerServiceClient;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.service.AuthService;
import com.g1.mychess.auth.service.TokenService;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Service class for handling authentication operations, including registration,
 * login, email verification, and password reset.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Constructs the AuthServiceImpl with necessary dependencies.
     */
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PlayerServiceClient playerServiceClient;
    private final AdminServiceClient adminServiceClient;
    private final EmailServiceClient emailServiceClient;
    private final TokenService tokenService;

    public AuthServiceImpl(
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            PlayerServiceClient playerServiceClient,
            AdminServiceClient adminServiceClient,
            EmailServiceClient emailServiceClient,
            TokenService tokenService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.playerServiceClient = playerServiceClient;
        this.adminServiceClient = adminServiceClient;
        this.emailServiceClient = emailServiceClient;
        this.tokenService = tokenService;
    }

    /**
     * Registers a new user by validating the registration data, creating the user in the player service,
     * generating a verification token, and sending an email verification link.
     *
     * @param registerRequestDTO The registration data for the new user.
     * @return ResponseEntity with a success message if registration is successful.
     * @throws InvalidPasswordException If the password does not meet security requirements.
     * @throws InvalidEmailException If the email format is invalid.
     * @throws UserAlreadyExistsException If a user with the same username or email already exists.
     * @throws PlayerServiceException If there is an issue with the player service during user creation.
     */
    @Override
    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        validateRegistrationData(registerRequestDTO);

        RegisterRequestDTO playerDTO = mapToPlayerDTO(registerRequestDTO);
        ResponseEntity<PlayerCreationResponseDTO> playerServiceResponse = playerServiceClient.createPlayer(playerDTO);

        if (!playerServiceResponse.getStatusCode().is2xxSuccessful()) {
            handlePlayerServiceError(playerServiceResponse);
        }

        String verificationToken = tokenService.generateToken(
                playerServiceResponse.getBody().getPlayerId(),
                "ROLE_PLAYER",
                UserToken.TokenType.EMAIL_VERIFICATION,
                LocalDateTime.now().plusDays(1)
        );
        sendVerificationEmail(registerRequestDTO.getEmail(), registerRequestDTO.getUsername(), verificationToken);

        return ResponseEntity.ok("Registration successful! Check your email to verify your account.");
    }

    /**
     * Logs in the user by validating credentials, checking for email verification, and generating a JWT token.
     *
     * @param username The username of the user attempting to log in.
     * @param password The password provided by the user.
     * @param role The role of the user (e.g., "ROLE_PLAYER").
     * @return The generated JWT token if login is successful.
     * @throws InvalidCredentialsException If the password does not match the stored password.
     * @throws EmailNotVerifiedException If the user's email is not verified.
     */
    @Override
    public String login(String username, String password, String role) {
        UserDTO userDTO = fetchUserByUsernameAndRole(username, role);
        validateLoginCredentials(password, userDTO);

        if (!tokenService.isEmailVerified(userDTO.getUserId(), role)) {
            throw new EmailNotVerifiedException("Please verify your email before logging in.");
        }

        return generateJwtToken(userDTO);
    }

    /**
     * Validates registration data such as password strength and email format.
     *
     * @param registerRequestDTO The registration data to be validated.
     * @throws InvalidPasswordException If the password does not meet security requirements.
     * @throws InvalidEmailException If the email format is invalid.
     */
    private void validateRegistrationData(RegisterRequestDTO registerRequestDTO) {
        if (!isValidPassword(registerRequestDTO.getPassword())) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one number.");
        }

        if (!isValidEmail(registerRequestDTO.getEmail())) {
            throw new InvalidEmailException("This is not a valid email address.");
        }
    }

    /**
     * Validates login credentials by comparing the provided password with the stored password.
     *
     * @param password The provided password.
     * @param userDTO The user data retrieved from the database.
     * @throws InvalidCredentialsException If the provided password does not match the stored password.
     */
    private void validateLoginCredentials(String password, UserDTO userDTO) {
        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }
    }

    /**
     * Generates a JWT token for the authenticated user based on their details and role.
     *
     * @param userDTO The authenticated user data.
     * @return The generated JWT token.
     */
    private String generateJwtToken(UserDTO userDTO) {
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(userDTO.getRole()))
        );
        return jwtUtil.generateToken(userDetails, userDTO.getUserId());
    }

    /**
     * Maps the registration data to a player-specific DTO with a hashed password.
     *
     * @param registerRequestDTO The original registration data.
     * @return A new RegisterRequestDTO with the password hashed.
     */
    private RegisterRequestDTO mapToPlayerDTO(RegisterRequestDTO registerRequestDTO) {
        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
        return new RegisterRequestDTO(
                registerRequestDTO.getUsername(),
                hashedPassword,
                registerRequestDTO.getEmail(),
                registerRequestDTO.getGender(),
                registerRequestDTO.getCountry(),
                registerRequestDTO.getRegion(),
                registerRequestDTO.getCity(),
                registerRequestDTO.getBirthDate()
        );
    }

    /**
     * Handles errors from the player service during registration, such as conflicts when a user already exists.
     *
     * @param response The response from the player service.
     * @throws UserAlreadyExistsException If a user with the same username or email already exists.
     * @throws PlayerServiceException If the player service fails with an unexpected error.
     */
    private void handlePlayerServiceError(ResponseEntity<PlayerCreationResponseDTO> response) {
        if (response.getStatusCode() == HttpStatus.CONFLICT) {
            PlayerCreationResponseDTO responseBody = response.getBody();
            if (responseBody != null) {
                throw new UserAlreadyExistsException(responseBody.getMessage());
            }
        }
        throw new PlayerServiceException("Player service failed to register the user. Status code: " + response.getStatusCode());
    }

    /**
     * Resends the email verification link if the user's email is not already verified.
     *
     * @param email The email address of the user.
     * @return ResponseEntity with a success message if the email is resent.
     * @throws EmailAlreadyVerifiedException If the user's email is already verified.
     */
    @Override
    public ResponseEntity<String> resendVerificationEmail(String email) {
        UserDTO userDTO = fetchUserByEmail(email);

        if (tokenService.isEmailVerified(userDTO.getUserId(), userDTO.getRole())) {
            throw new EmailAlreadyVerifiedException("Your email is already verified. Proceed to login!");
        }

        String verificationToken = tokenService.generateToken(userDTO.getUserId(), userDTO.getRole(), UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));

        sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), verificationToken);

        return ResponseEntity.ok("Verification email resent successfully.");
    }

    /**
     * Sends a password reset link to the user's email address.
     *
     * @param email The email address of the user.
     * @return ResponseEntity with a success message if the reset email is sent.
     * @throws UserNotFoundException If no user is found with the provided email.
     */
    @Override
    public ResponseEntity<String> requestPasswordReset(String email) {
        UserDTO userDTO = fetchUserByEmail(email);

        String resetToken = generateResetToken(userDTO);

        sendPasswordResetEmail(userDTO.getEmail(), userDTO.getUsername(), resetToken);

        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    /**
     * Resets the user's password after validating the reset token.
     *
     * @param resetToken The token to authorize password reset.
     * @param newPassword The new password to be set.
     * @return ResponseEntity with a success message if the password reset is successful.
     * @throws InvalidPasswordException If the new password does not meet security requirements.
     * @throws UserTokenException If the reset token is invalid or expired.
     * @throws UserNotFoundException If no user is found for the reset token.
     */
    @Override
    public ResponseEntity<String> resetPassword(String resetToken, String newPassword) {
        validatePasswordReset(newPassword);

        UserToken token = tokenService.validateToken(resetToken, UserToken.TokenType.PASSWORD_RESET);

        UserDTO userDTO = fetchUserById(token.getUserId(), token.getUserType());

        String hashedPassword = passwordEncoder.encode(newPassword);

        updatePassword(userDTO.getUserId(), hashedPassword);

        tokenService.markTokenAsUsed(token);

        return ResponseEntity.ok("Password has been reset successfully.");
    }

    /**
     * Verifies the user's email by validating the token and marking it as used.
     *
     * @param token The email verification token.
     * @throws UserTokenException If the token is invalid or expired.
     */
    @Override
    public void verifyEmail(String token) {
        UserToken userToken = tokenService.validateToken(token, UserToken.TokenType.EMAIL_VERIFICATION);
        tokenService.markTokenAsUsed(userToken);
    }

    /**
     * Checks if the user's email is verified.
     *
     * @param userId The unique identifier of the user.
     * @param userType The role of the user (e.g., "ROLE_PLAYER").
     * @return true if the email is verified; false otherwise.
     */
    @Override
    public boolean isEmailVerified(Long userId, String userType) {
        return tokenService.isEmailVerified(userId, userType);
    }

    /**
     * Generates a password reset token with a 1-hour expiry time for the specified user.
     *
     * @param userDTO The user data for whom the reset token is generated.
     * @return The generated password reset token.
     */
    private String generateResetToken(UserDTO userDTO) {
        return tokenService.generateToken(
                userDTO.getUserId(),
                userDTO.getRole(),
                UserToken.TokenType.PASSWORD_RESET,
                LocalDateTime.now().plusHours(1)
        );
    }

    /**
     * Fetches the user data by username and role, throwing an exception if the user is not found.
     *
     * @param username The username of the user.
     * @param role The role of the user (e.g., "ROLE_PLAYER").
     * @return The user data for the specified username and role.
     * @throws UserNotFoundException If no user is found with the provided username and role.
     * @throws InvalidRoleException If an invalid role is provided.
     */
    private UserDTO fetchUserByUsernameAndRole(String username, String role) {
        UserDTO userDTO;
        if ("ROLE_PLAYER".equals(role)) {
            userDTO = playerServiceClient.fetchPlayerByUsername(username);
        } else if ("ROLE_ADMIN".equals(role)) {
            userDTO = adminServiceClient.fetchAdminByUsername(username);
        } else {
            throw new InvalidRoleException("Invalid role provided.");
        }

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        return userDTO;
    }

    /**
     * Fetches the user data by email, throwing an exception if the user is not found.
     *
     * @param email The email address of the user.
     * @return The user data for the specified email.
     * @throws UserNotFoundException If no user is found with the provided email.
     */
    private UserDTO fetchUserByEmail(String email) {
        UserDTO userDTO = playerServiceClient.fetchPlayerByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User with this email not found.");
        }

        return userDTO;
    }

    /**
     * Fetches the user data by ID and role, throwing an exception if the user is not found.
     *
     * @param userId The unique identifier of the user.
     * @param role The role of the user (e.g., "ROLE_PLAYER").
     * @return The user data for the specified ID and role.
     * @throws UserNotFoundException If no user is found with the provided ID and role.
     * @throws InvalidRoleException If an invalid role is provided.
     */
    private UserDTO fetchUserById(Long userId, String role) {
        UserDTO userDTO;
        if ("ROLE_PLAYER".equals(role)) {
            userDTO = playerServiceClient.fetchPlayerById(userId);
        } /* else if ("ROLE_ADMIN".equals(role)) {
            userDTO = adminServiceClient.fetchAdminById(userId);
        } */ else {
            throw new InvalidRoleException("Invalid role provided.");
        }

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        return userDTO;
    }

    /**
     * Updates the password for the user with the specified ID.
     *
     * @param playerId       The unique identifier of the player whose password needs to be updated.
     * @param hashedPassword The new hashed password to set for the user.
     */
    private void updatePassword(Long playerId, String hashedPassword) {
        playerServiceClient.updatePassword(playerId, hashedPassword);
    }

    /**
     * Validates the new password based on minimum security requirements.
     *
     * @param newPassword The new password to validate.
     * @throws InvalidPasswordException If the password does not meet security requirements.
     */
    private void validatePasswordReset(String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new InvalidPasswordException("Password does not meet the requirements.");
        }
    }

    /**
     * Sends an email verification link to the user.
     *
     * @param email The email address of the user.
     * @param username The username of the user.
     * @param verificationToken The token to include in the verification link.
     */
    private void sendVerificationEmail(String email, String username, String verificationToken) {
        sendEmail(email, username, verificationToken, EmailServiceClient::sendVerificationEmail);
    }

    /**
     * Sends a password reset email to the user.
     *
     * @param email The email address of the user.
     * @param username The username of the user.
     * @param resetToken The token to include in the password reset link.
     */
    private void sendPasswordResetEmail(String email, String username, String resetToken) {
        sendEmail(email, username, resetToken, EmailServiceClient::sendPasswordResetEmail);
    }

    /**
     * Helper method to send an email using a specified function.
     *
     * @param email The email address of the recipient.
     * @param username The username of the recipient.
     * @param token The token to include in the email content.
     * @param sendFunction The function that sends the email using the EmailServiceClient.
     */
    private void sendEmail(String email, String username, String token, EmailServiceClientEmailFunction sendFunction) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setUserToken(token);
        sendFunction.apply(emailServiceClient, emailRequestDTO);
    }

    /**
     * Validates if the password meets minimum security requirements.
     *
     * @param password The password to validate.
     * @return true if the password meets security requirements; false otherwise.
     */
    public boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }

    /**
     * Validates if the provided email format is correct.
     *
     * @param email The email address to validate.
     * @return true if the email format is valid; false otherwise.
     */
    public boolean isValidEmail(String email) {
        String EMAIL_REGEX = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+" +
                "(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|" +
                "\"(?:[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E]|" +
                "\\\\[!#$%&'*+/=?^_`{|}~\\-\\x20-\\x7E])*\")@" +
                "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+" +
                "[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
        return email.matches(EMAIL_REGEX);
    }

    /**
     * Functional interface for defining custom email sending actions, such as sending a verification
     * or password reset email.
     *
     * <p>This interface allows the email-sending method to accept different email functions as parameters,
     * enabling flexible invocation of specific email notifications based on the provided function.</p>
     */
    @FunctionalInterface
    interface EmailServiceClientEmailFunction {
        void apply(EmailServiceClient client, EmailRequestDTO emailRequestDTO);
    }
}