package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {

    private final WebClient.Builder webClientBuilder;
    private final PasswordEncoder passwordEncoder;
//    private final UserRepository userRepository;

    @Autowired
    public AuthService(WebClient.Builder webClientBuilder, PasswordEncoder passwordEncoder) {
        this.webClientBuilder = webClientBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    // Registration method
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

            ResponseEntity<String> userServiceResponse = webClientBuilder.build()
                    .post()
                    .uri("http://localhost:8081/users")
                    .bodyValue(userDTO)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (userServiceResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok("Registration successful!");
            } else if (userServiceResponse.getStatusCode() == HttpStatus.CONFLICT) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed.");
            }
        }

        private boolean isValidPassword(String password) {
            return password.length() >= 8 && password.matches(".*\\d.*");
        }

}



    // // Login method with JWT generation
    // public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
    //     // Logic for user login and JWT generation

    //     // Example: External API call for login (if needed)
    //     Mono<String> response = webClient.post()
    //             .uri("/external-api/authenticate")
    //             .bodyValue(loginRequest)
    //             .retrieve()
    //             .bodyToMono(String.class);

    //     // Generate JWT and return response

    //     return ResponseEntity.ok("User logged in successfully!");
    // }

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
    // public ResponseEntity<? > verifyEmail(VerifyEmailRequest verifyEmailRequest) {
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
