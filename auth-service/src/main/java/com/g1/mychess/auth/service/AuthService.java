package com.g1.mychess.auth.service;

import com.g1.mychess.auth.dto.*;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<String> registerUser(RegisterRequestDTO registerRequestDTO);

    String login(String username, String password, String role);

    ResponseEntity<String> resendVerificationEmail(String email);

    ResponseEntity<String> requestPasswordReset(String email);

    ResponseEntity<String> resetPassword(String resetToken, String newPassword);

    void verifyEmail(String token);

    boolean isEmailVerified(Long userId, String userType);
}