package com.g1.mychess.auth.controller;

import com.g1.mychess.auth.model.*;
import com.g1.mychess.auth.service.*;
import com.g1.mychess.auth.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final WebClient.Builder webClientBuilder;

    @Autowired
    public AuthController(AuthService authService, WebClient.Builder webClientBuilder) {
        this.authService = authService;
        this.webClientBuilder = webClientBuilder;
    }

    // POST /auth/register: User registration (including email/phone verification)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.registerUser(registerRequestDTO);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = authService.verifyEmail(token);
        if (isVerified) {
            return ResponseEntity.ok("Email successfully verified!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification token.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            // Call the AuthService login method
            String token = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

            // Create response with the token
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login");
        }
    }

//    @GetMapping("/resend-verification")
//    public ResponseEntity<String> resendVerificationEmail(@RequestParam("userId") Long userId) {
//        // Check if the user is already verified
//        if (authService.isEmailVerified(userId)) {
//            return ResponseEntity.ok("Email already verified.");
//        }
//
//        // Fetch the username from the user microservice based on userId
//        String username;
//        try {
//            username = authService.fetchUsernameFromUserId(userId);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//        }
//
//        // Resend verification email logic
//        authService.sendVerificationEmail(username, userId);
//
//        return ResponseEntity.ok("Verification email sent.");
//    }

    // // POST /auth/login: User login with JWT generation
    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    //     return authService.loginUser(loginRequest);
    // }

    // // POST /auth/refresh-token: Refresh JWT tokens
    // @PostMapping("/refresh-token")
    // public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    //     return authService.refreshToken(refreshTokenRequest);
    // }

    // // POST /auth/verify-email: Verify email with token
    // @PostMapping("/verify-email")
    // public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequestDTO verifyEmailRequestDTO) {
    //     return authService.verifyEmail(verifyEmailRequest);
    // }

    // // POST /auth/request-password-reset: Initiate password reset
    // @PostMapping("/request-password-reset")
    // public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest) {
    //     return authService.requestPasswordReset(passwordResetRequest);
    // }

    // // POST /auth/reset-password: Complete password reset
    // @PostMapping("/reset-password")
    // public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
    //     return authService.resetPassword(resetPasswordRequest);
    // }

    // // POST /auth/logout: User logout
    // @PostMapping("/logout")
    // public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
    //     return authService.logoutUser(logoutRequest);
    // }
}

