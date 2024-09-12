package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.model.UserToken;
import com.g1.mychess.auth.repository.UserTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final WebClient.Builder webClientBuilder;
    private final PasswordEncoder passwordEncoder;

    private final UserTokenRepository userTokenRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder, PasswordEncoder passwordEncoder, UserTokenRepository userTokenRepository, JwtUtil jwtUtil) {
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
        this.userTokenRepository = userTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {

        String password = registerRequestDTO.getPassword();

        if (!isValidPassword(password)) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one number.");
        }

        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        RegisterRequestDTO userDTO = new RegisterRequestDTO(
                registerRequestDTO.getUsername(),
                hashedPassword,
                registerRequestDTO.getEmail()
        );

        ResponseEntity<UserCreationResponseDTO> userServiceResponse = webClientBuilder.build()
                .post()
                .uri("http://user-service:8081/api/v1/users")
                .bodyValue(userDTO)
                .retrieve()
                .toEntity(UserCreationResponseDTO.class)
                .block();

        if (userServiceResponse == null) {
            throw new UserServiceException("No response from user service.");
        }

        if (userServiceResponse.getStatusCode().is2xxSuccessful()) {
            UserCreationResponseDTO responseBody = userServiceResponse.getBody();

            if (responseBody != null && responseBody.getId() != null) {
                // Generate verification token and send the email
                String verificationToken = generateToken(responseBody.getId(), UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));
                try {
                    sendVerificationEmail(registerRequestDTO.getEmail(), registerRequestDTO.getUsername(), verificationToken);
                } catch (Exception e) {
                    throw new EmailSendFailedException("Failed to send verification email.");
                }
                return ResponseEntity.ok("Registration successful! Check your email to verify your account.");
            } else {
                throw new UserServiceException("userId is missing in the response from the user service.");
            }
        }

        if (userServiceResponse.getStatusCode() == HttpStatus.CONFLICT) {
            UserCreationResponseDTO responseBody = userServiceResponse.getBody();
            if (responseBody != null) {
                throw new UserAlreadyExistsException(responseBody.getMessage());
            }
        }

        throw new UserServiceException("User service failed to register the user. Status code: " + userServiceResponse.getStatusCode());
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }

    public String login(String username, String password) {
        // Fetch user details from User microservice
        UserDTO userDTO = fetchUserFromUserService(username);

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        if (!passwordEncoder.matches(password, userDTO.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        Long userId = userDTO.getId();

        if (!isEmailVerified(userId)) {
            throw new EmailNotVerifiedException("Please verify your email before logging in.");
        }

        // Generate JWT token after successful authentication
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                userDTO.getUsername(),
                userDTO.getPassword(),
                Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(userDTO.getRole()))
        );

        return jwtUtil.generateToken(userDetails);
    }

    private String generateToken(Long userId, UserToken.TokenType tokenType, LocalDateTime expirationTime) {
        try {
            // Check if there's already a token for this userId and tokenType
            Optional<UserToken> existingToken = userTokenRepository.findByUserIdAndTokenType(userId, tokenType);

            // If a token exists, delete it
            existingToken.ifPresent(userTokenRepository::delete);

            // Generate a new token
            String token = UUID.randomUUID().toString();

            // Create a new UserToken object
            UserToken userToken = new UserToken(
                    token,
                    expirationTime,
                    tokenType,
                    userId
            );

            // Save the new token to the database
            userTokenRepository.save(userToken);

            // Return the newly generated token
            return token;
        } catch (Exception e) {
            throw new UserTokenException("Failed to generate or store verification token.");
        }
    }


    public void sendVerificationEmail(String email, String username, String verificationToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setUserToken(verificationToken);

        try {
            webClientBuilder.build()
                    .post()
                    .uri("http://email-service:8085/api/v1/email/send-verification")
                    .bodyValue(emailRequestDTO)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send the verification email.");
        }
    }

    public ResponseEntity<String> resendVerificationEmail(String email) {
        UserDTO userDTO = fetchUserFromUserServiceByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User with this email not found.");
        }

        Long userId = userDTO.getId();

        // Check if the email is already verified
        if (isEmailVerified(userId)) {
            throw new EmailAlreadyVerifiedException("Your email is already verified. Proceed to login!");
        }

        // Generate a new verification token and send the verification email
        String verificationToken = generateToken(userId, UserToken.TokenType.EMAIL_VERIFICATION, LocalDateTime.now().plusDays(1));
        try {
            sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), verificationToken);
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send verification email.");
        }

        return ResponseEntity.ok("Verification email resent successfully.");
    }

    public ResponseEntity<String> requestPasswordReset(String email) {
        UserDTO userDTO = fetchUserFromUserServiceByEmail(email);

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        Long userId = userDTO.getId();
        String resetToken = generateToken(userId, UserToken.TokenType.PASSWORD_RESET, LocalDateTime.now().plusHours(1));

        try {
            sendVerificationEmail(userDTO.getEmail(), userDTO.getUsername(), resetToken);
        } catch (Exception e) {
            throw new EmailSendFailedException("Failed to send password reset email.");
        }

        return ResponseEntity.ok("Password reset email sent successfully.");
    }

    public ResponseEntity<String> resetPassword(String resetToken, String newPassword) {
        if (!isValidPassword(newPassword)) {
            throw new InvalidPasswordException("Password does not meet the requirements.");
        }

        UserToken token = userTokenRepository.findByTokenAndTokenType(resetToken, UserToken.TokenType.PASSWORD_RESET)
                .orElseThrow(() -> new UserTokenException("Invalid reset token."));

        if (token.isUsed()) {
            throw new UserTokenException("Reset token already used.");
        }

        if (token.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserTokenException("Token has expired. Please request password reset again.");
        }

        // Fetch user by ID and update password
        UserDTO userDTO = fetchUserFromUserServiceById(token.getUserId());

        if (userDTO == null) {
            throw new UserNotFoundException("User not found.");
        }

        String hashedPassword = passwordEncoder.encode(newPassword);

        // Send the new password to the user service to update the user's password
            updatePasswordInUserService(userDTO.getId(), hashedPassword);

        // Mark token as used
        token.setUsed(true);
        userTokenRepository.save(token);

        return ResponseEntity.ok("Password has been reset successfully.");
    }

    public UserDTO fetchUserFromUserService(String username) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service:8081/api/v1/users/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking call for simplicity
    }

    public UserDTO fetchUserFromUserServiceByEmail(String email) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service:8081/api/v1/users/email/" + email)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking call for simplicity
    }

    public UserDTO fetchUserFromUserServiceById(Long userId) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service:8081/api/v1/users/userId/" + userId)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking call for simplicity
    }

    private void updatePasswordInUserService(Long userId, String hashedPassword) {
        // Create a DTO or request body to send the updated password
        UpdatePasswordRequestDTO updatePasswordRequest = new UpdatePasswordRequestDTO(userId, hashedPassword);

        // Make the PUT request to the user service to update the user's password
        try {
            webClientBuilder.build()
                    .put()
                    .uri("http://user-service:8081/api/v1/users/update-password") // Adjust the URI based on your actual user service endpoint
                    .bodyValue(updatePasswordRequest)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();  // Blocking call for simplicity; you could use async if needed
        } catch (Exception e) {
            throw new UserServiceException("Failed to update the user's password.");
        }
    }

    public void verifyEmail(String token) {
        UserToken userToken = userTokenRepository.findByTokenAndTokenType(token, UserToken.TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new UserTokenException("Token is invalid."));

        if (userToken.isUsed()) {
            throw new UserTokenException("Your email is already verified. You can proceed to log in.");
        }

        if (userToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new UserTokenException("Token is expired. Please request a new verification email.");
        }

        userToken.setUsed(true);
        userTokenRepository.save(userToken);
    }

    public boolean isEmailVerified(Long userId) {
        // Check if the user has a valid, used verification token
        UserToken userToken = userTokenRepository.findByUserIdAndTokenTypeAndUsed(userId, UserToken.TokenType.EMAIL_VERIFICATION, true)
                .orElse(null);

        return userToken != null; // If the used token exists, the email is verified
    }
}

    // // Refresh token method
    // public ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest) {
    //     // Logic for refreshing JWT tokens

    //     // Example: External API call for refreshing token
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/refresh-token")
    //             .bodyValue(refreshTokenRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     return ResponseEntity.ok("Token refreshed successfully!");
    // }

    // // Email verification method
    // public ResponseEntity<? > verifyEmail(VerifyEmailRequestDTO verifyEmailRequest) {
    //     // Logic for email verification

    //     // Example: External API call for email verification
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/verify-email")
    //             .bodyValue(verifyEmailRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     return ResponseEntity.ok("Email verified successfully!");
    // }

    // // Request password reset
    // public ResponseEntity<?> requestPasswordReset(PasswordResetRequest passwordResetRequest) {
    //     // Logic for initiating password reset

    //     // Example: External API call for password reset request
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/request-password-reset")
    //             .bodyValue(passwordResetRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     return ResponseEntity.ok("Password reset request sent successfully!");
    // }

    // // Complete password reset
    // public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
    //     // Logic for completing password reset

    //     // Example: External API call for completing password reset
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/reset-password")
    //             .bodyValue(resetPasswordRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     return ResponseEntity.ok("Password reset successfully!");
    // }

    // // Logout method
    // public ResponseEntity<?> logoutUser(LogoutRequest logoutRequest) {
    //     // Logic for logging out user

    //     // Example: External API call for logging out (if needed)
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/logout")
    //             .bodyValue(logoutRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     return ResponseEntity.ok("User logged out successfully!");
    // }
