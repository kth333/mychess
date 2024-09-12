package com.g1.mychess.auth.controller;

import com.g1.mychess.auth.model.*;
import com.g1.mychess.auth.service.*;
import com.g1.mychess.auth.dto.*;
import com.g1.mychess.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
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

    // POST /auth/register: User registration (including email/phone verification)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequestDTO registerRequestDTO) {
        return authService.registerUser(registerRequestDTO);
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        authService.verifyEmail(token);
        return ResponseEntity.ok("Email successfully verified!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        String token = authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/resend-verification-email")
    public ResponseEntity<String> resendVerificationEmail(@RequestBody String email) {
        return authService.resendVerificationEmail(email);
    }

    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody String email) {
        return authService.requestPasswordReset(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequest) {
        return authService.resetPassword(resetPasswordRequest.getResetToken(), resetPasswordRequest.getNewPassword());
    }

    @PostMapping("/validate-jwt")
    public ResponseEntity<AuthResponseDTO> validateJwt(@RequestBody String token) {
        // Extract username from the token
        String username = jwtUtil.extractUsername(token);

        // Validate the token and check if it's valid
        if (username != null && jwtUtil.validateToken(token)) {
            // Extract roles and return as GrantedAuthority
            List<GrantedAuthority> authorities = jwtUtil.extractRoles(token); // Returns List<GrantedAuthority>

            // Create AuthResponseDTO and return it
            AuthResponseDTO response = new AuthResponseDTO(username, authorities);
            return ResponseEntity.ok(response);
        } else {
            // Return unauthorized status if token is invalid
            return ResponseEntity.status(401).build();
        }
    }
}

