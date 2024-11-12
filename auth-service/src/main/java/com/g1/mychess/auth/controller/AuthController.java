package com.g1.mychess.auth.controller;

import com.g1.mychess.auth.service.*;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for handling authentication-related operations such as registration,
 * login, email verification, password reset, and health check.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    /**
     * Constructs the AuthController with the required services and utilities.
     *
     * @param authService      the authentication service for handling auth operations
     * @param webClientBuilder the web client builder for external requests
     * @param jwtUtil          the utility for handling JWT operations
     */
    @Autowired
    public AuthController(AuthService authService, WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.authService = authService;
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a new user.
     *
     * @param registerRequestDTO the registration details
     * @return ResponseEntity containing a success message or error details
     */
    @PostMapping("/registration")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.registerUser(registerRequestDTO);
    }

    /**
     * Verifies the user's email using a token.
     *
     * @param token the token for verifying the email
     * @return ResponseEntity with a success message upon successful verification
     */
    @GetMapping("/verification")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") @Valid String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email successfully verified!");
    }

    /**
     * Authenticates a user and returns a JWT token on successful login.
     *
     * @param loginRequestDTO the login request containing username, password, and role
     * @return ResponseEntity with a JWT token or an error message if login fails
     */
    @PostMapping("/session/new")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword(), loginRequestDTO.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    /**
     * Resends the verification email to the user.
     *
     * @param email the email address to resend the verification email to
     * @return ResponseEntity with a success or error message
     */
    @PostMapping("/verification-requests")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody @Valid String email) {
        return authService.resendVerificationEmail(email);
    }

    /**
     * Initiates a password reset request for the specified email.
     *
     * @param email the email address to send the password reset link to
     * @return ResponseEntity with a message indicating the result of the reset request
     */
    @PostMapping("/password-recovery/{email}")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String email) {
        return authService.requestPasswordReset(email);
    }

    /**
     * Resets the user's password using a reset token and a new password.
     *
     * @param resetPasswordRequest DTO containing the reset token and new password
     * @return ResponseEntity with a success message upon successful password reset
     */
    @PostMapping("/password-recovery")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest.getResetToken(), resetPasswordRequest.getNewPassword());
    }

    /**
     * Health check endpoint to verify that the service is operational.
     *
     * @return ResponseEntity with a message indicating the service status
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
