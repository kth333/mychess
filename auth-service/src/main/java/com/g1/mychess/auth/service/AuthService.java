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
import java.util.Map;
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

        System.out.println("Response Status: " + userServiceResponse.getStatusCode());
        System.out.println("Response Body: " + userServiceResponse.getBody());

        if (userServiceResponse.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> responseBody = userServiceResponse.getBody();

            if (responseBody != null && responseBody.containsKey("userId")) {
                // Try to handle userId whether it's an Integer or Long
                Object userIdObj = responseBody.get("userId");

                Long userId = null;
                if (userIdObj instanceof Integer) {
                    userId = ((Integer) userIdObj).longValue();  // Cast Integer to Long
                } else if (userIdObj instanceof Long) {
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
            System.out.println("Authentication successful for user: " + username);

            // Authenticate the user using the provided credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            System.out.println("Authentication successful for user: " + username);
            System.out.println("Authentication object: " + authentication);

            // Fetch user details from User microservice
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            System.out.println("User details fetched: " + userDetails.getUsername());

            Long userId = fetchUserIdFromUserService(username);  // Fetch the user ID from User service
            System.out.println("User ID fetched from user service: " + userId);

            // Check if email is verified using VerificationToken
            if (!isEmailVerified(userId)) {
                System.out.println("Email not verified for user ID: " + userId);

                throw new IllegalStateException("Email is not verified. Please verify your email before logging in.");
            }

            System.out.println("Email verified for user ID: " + userId);

            // Generate JWT token after successful authentication and email verification
            return jwtUtil.generateToken(userDetails);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password.");
        } catch (IllegalStateException e) {
            // Re-throw the exception if email is not verified with a descriptive message
            System.out.println("Error: " + e.getMessage());
            throw new IllegalStateException(e.getMessage());
        } catch (Exception e) {
            // Catch any other exceptions and throw a generic exception
            System.out.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();  // Print the stack trace for more information
            throw new Exception("An error occurred during login.", e);
        }
    }

    private String generateVerificationToken(Long userId) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken(
                token,
                LocalDateTime.now().plusDays(1),  // Set expiration to 1 day
                VerificationToken.TokenType.EMAIL_VERIFICATION,
                userId
        );
        verificationTokenRepository.save(verificationToken);

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

    private Long fetchUserIdFromUserService(String username) {
        // Make a call to the User microservice to retrieve the user's ID
        String url = "http://localhost:8081/api/v1/users/userId/" + username; // Assuming this is your User service endpoint
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(Long.class)
                .block();  // Synchronous block for simplicity, but you can use reactive if desired
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
