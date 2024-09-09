package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.repository.VerificationTokenRepository;
import com.g1.mychess.auth.util.JwtUtil;
import com.g1.mychess.auth.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final AuthenticationManager authenticationManager;

    private final VerificationTokenRepository verificationTokenRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, VerificationTokenRepository verificationTokenRepository, JwtUtil jwtUtil) {
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.verificationTokenRepository = verificationTokenRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {

        String password = registerRequestDTO.getPassword();

        if (!isValidPassword(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password must be at least 8 characters long and contain at least one number.");
        }

        String hashedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        RegisterRequestDTO userDTO = new RegisterRequestDTO(
                registerRequestDTO.getUsername(),
                hashedPassword,
                registerRequestDTO.getEmail()
        );

        ResponseEntity<Map<String, Object>> userServiceResponse = webClientBuilder.build()
                .post()
                .uri("http://localhost:8081/api/v1/users")
                .bodyValue(userDTO)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();

        if (userServiceResponse.getStatusCode().is2xxSuccessful()) {

            Map<String, Object> responseBody = userServiceResponse.getBody();

            if (responseBody != null && responseBody.containsKey("userId")) {
                Object userIdObj = responseBody.get("userId");
                Long userId = null;
                if (userIdObj instanceof Integer) {
                    userId = ((Integer) userIdObj).longValue();
                } else {
                    userId = (Long) userIdObj;
                }

                if (userId != null) {
                    // Generate verification token and send the email
                    String verificationToken = generateVerificationToken(userId);
                    sendVerificationEmail(registerRequestDTO.getEmail(), registerRequestDTO.getUsername(), verificationToken);
                    return ResponseEntity.ok("Registration successful! Check your email to verify your account.");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve userId.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("userId is missing in the response.");
            }
        } else if (userServiceResponse.getStatusCode() == HttpStatus.CONFLICT) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 && password.matches(".*\\d.*");
    }

    public String login(String username, String password) throws Exception {
        try {
            // Fetch user details from User microservice
            UserDTO userDTO = fetchUserFromUserService(username);
            if (userDTO == null) {
                throw new Exception("User not found.");
            }

            if (!passwordEncoder.matches(password, userDTO.getPassword())) {
                System.out.println("Invalid credentials for user: " + username);
                throw new Exception("Invalid username or password.");
            }

            Long userId = userDTO.getId();

            if (!isEmailVerified(userId)) {
                throw new IllegalStateException("Please verify your email before logging in.");
            }

            // Generate JWT token after successful authentication
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    userDTO.getUsername(),
                    userDTO.getPassword(),
                    Collections.singleton(new org.springframework.security.core.authority.SimpleGrantedAuthority(userDTO.getRole()))
            );

            String token = jwtUtil.generateToken(userDetails);

            return token;
        } catch (Exception e) {
            throw new Exception("An error occurred during login.", e);
        }

    }

    private String generateVerificationToken(Long userId) {
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
    }


    public void sendVerificationEmail(String email, String username, String verificationToken) {
        EmailRequestDTO emailRequestDTO = new EmailRequestDTO();
        emailRequestDTO.setTo(email);
        emailRequestDTO.setUsername(username);
        emailRequestDTO.setVerificationToken(verificationToken);

        webClientBuilder.build()
                .post()
                .uri("http://localhost:8085/api/v1/email/send-verification")
                .bodyValue(emailRequestDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public UserDTO fetchUserFromUserService(String username) {
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/api/v1/users/username/" + username)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block(); // Blocking call for simplicity
    }

    public boolean verifyEmail(String token) {
        // Find the verification token by token string
        VerificationToken verificationToken = verificationTokenRepository.findByTokenAndTokenType(token, VerificationToken.TokenType.EMAIL_VERIFICATION)
                .orElse(null);

        if (verificationToken == null || verificationToken.isUsed() || verificationToken.getExpirationTime().isBefore(LocalDateTime.now())) {
            return false; // Token is invalid, expired, or already used
        }

        // Mark the token as used
        verificationToken.setUsed(true);
        verificationTokenRepository.save(verificationToken);

        return true; // Email is successfully verified
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
