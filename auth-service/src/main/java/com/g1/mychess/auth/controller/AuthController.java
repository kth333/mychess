package com.g1.mychess.auth.controller;

import com.g1.mychess.auth.model.*;
import com.g1.mychess.auth.service.*;
import com.g1.mychess.auth.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final WebClient.Builder webClientBuilder;

    // Constructor-based injection

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
    // public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailRequest verifyEmailRequest) {
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

