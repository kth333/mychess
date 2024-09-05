package com.g1.mychess.auth.service;

import com.g1.mychess.user.model.User;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@Service
public class AuthService {

//    private final WebClient webClient;
    private final UserRepository userRepository;

    @Autowired
    public AuthService(/*WebClient webClient*/UserRepository userRepository) {
//        this.webClient = webClient;
        this.userRepository = userRepository;
    }

    // Registration method
    public ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO) {
        // Logic for user registration, including email/phone verification
            
    
        User u = new User(registerRequestDTO.getUsername(), registerRequestDTO.getPassword(), registerRequestDTO.getEmail(), User.Role.PLAYER);
           

        // // Example: External API call using WebClient (if needed)
        // Mono<String> response = webClient.post()
        //         .uri("/external-api/verify-email")
        //         .bodyValue(registerRequest)
        //         .retrieve()
        //         .bodyToMono(String.class);

        // Further registration logic, such as saving user to database
        userRepository.save(u); 
        return ResponseEntity.ok("User registered successfully!");
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
}
