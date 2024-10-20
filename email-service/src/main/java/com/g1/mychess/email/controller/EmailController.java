package com.g1.mychess.email.controller;

import com.g1.mychess.email.dto.BlacklistEmailDTO;
import com.g1.mychess.email.dto.WhitelistEmailDTO;
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
            emailService.sendVerificationEmail(emailRequestDTO.getTo(), emailRequestDTO.getUsername(), emailRequestDTO.getUserToken());
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification email.");
        }
    }

    @PostMapping("/send-blacklist")
    public ResponseEntity<String> sendBlacklistEmail(@RequestBody BlacklistEmailDTO blacklistEmailDTO) {
        try {
            emailService.sendBlacklistEmail(
                    blacklistEmailDTO.getTo(),
                    blacklistEmailDTO.getUsername(),
                    blacklistEmailDTO.getReason(),
                    blacklistEmailDTO.getBanDuration()
            );
            return ResponseEntity.ok("Blacklist email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send blacklist email.");
        }
    }

    // Method to send whitelist email
    @PostMapping("/send-whitelist")
    public ResponseEntity<String> sendWhitelistEmail(@RequestBody WhitelistEmailDTO whitelistEmailDTO) {
        try {
            emailService.sendWhitelistEmail(
                    whitelistEmailDTO.getTo(),
                    whitelistEmailDTO.getUsername(),
                    whitelistEmailDTO.getReason()
            );
            return ResponseEntity.ok("Whitelist email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send whitelist email.");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}
