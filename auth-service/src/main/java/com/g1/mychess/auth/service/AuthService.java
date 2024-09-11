package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.exception.*;
import com.g1.mychess.auth.repository.VerificationTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;
import com.g1.mychess.auth.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final WebClient.Builder webClientBuilder;
    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository verificationTokenRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder, PasswordEncoder passwordEncoder, VerificationTokenRepository verificationTokenRepository, JwtUtil jwtUtil) {
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
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
                String verificationToken = generateVerificationToken(responseBody.getId());
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
            throw new UserAlreadyExistsException("User with the given email or username already exists.");
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

    private String generateVerificationToken(Long userId) {
        try {
            // First, check if there's already a token for this userId
            Optional<VerificationToken> existingToken = verificationTokenRepository.findByUserIdAndTokenType(userId, VerificationToken.TokenType.EMAIL_VERIFICATION);

            // If a token exists, remove it
            existingToken.ifPresent(verificationTokenRepository::delete);

            // Generate a new token
            String token = UUID.randomUUID().toString();

            // Create a new VerificationToken object
            VerificationToken verificationToken = new VerificationToken(
                    token,
                    LocalDateTime.now().plusDays(1),  // Set expiration to 1 day
                    VerificationToken.TokenType.EMAIL_VERIFICATION,
                    userId
            );

            // Save the new token to the database
            verificationTokenRepository.save(verificationToken);

            // Return the newly generated token
            return token;
        } catch (Exception e) {
            throw new VerificationTokenException("Failed to generate or store verification token.");
        }
    }

    public void sendVerificationEmail(String email, String username, String verificationToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setVerificationToken(verificationToken);

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

    public UserDTO fetchUserFromUserService(String username) {
        return webClientBuilder.build()
                .get()
                .uri("http://user-service:8081/api/v1/users/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking call for simplicity
    }

    public void verifyEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndTokenType(token, VerificationToken.TokenType.EMAIL_VERIFICATION)
                .orElseThrow(() -> new VerificationTokenException("Token is invalid."));

        if (verificationToken.isUsed()) {
            throw new VerificationTokenException("Your email is already verified. You can proceed to log in.");
        }

        if (verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenException("Token is expired. Please request a new verification email.");
        }

        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);
    }

    public boolean isEmailVerified(Long userId) {
        // Check if the user has a valid, used verification token
        VerificationToken verificationToken = verificationTokenRepository.findByUserIdAndTokenTypeAndUsed(userId, VerificationToken.TokenType.EMAIL_VERIFICATION, true)
                .orElse(null);

        return verificationToken != null; // If the used token exists, the email is verified
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
