package com.g1.mychess.auth.controller;

import com.g1.mychess.auth.service.*;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final WebClient.Builder webClientBuilder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, WebClient.Builder webClientBuilder, JwtUtil jwtUtil) {
        this.authService = authService;
        this.webClientBuilder = webClientBuilder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.registerUser(registerRequestDTO);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") @Valid String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email successfully verified!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword(), loginRequestDTO.getRole());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody @Valid String email) {
        return authService.resendVerificationEmail(email);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid String email) {
        return authService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest.getResetToken(), resetPasswordRequest.getNewPassword());
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}