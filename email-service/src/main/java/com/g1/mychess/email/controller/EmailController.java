package com.g1.mychess.email.controller;

import com.g1.mychess.email.service.EmailService;
import com.g1.mychess.email.dto.EmailRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        try {
            emailService.sendVerificationEmail(emailRequestDTO.getTo(), emailRequestDTO.getUsername(), emailRequestDTO.getVerificationToken());
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification email.");
        }
    }
}
