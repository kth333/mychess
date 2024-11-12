package com.g1.mychess.email.controller;

import com.g1.mychess.email.dto.*;
import com.g1.mychess.email.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling email-related requests.
 * This includes endpoints for verification, blacklist, whitelist, password reset, feedback,
 * and tournament notifications. Each endpoint sends a specific type of email.
 */
@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    private final EmailService emailService;

    /**
     * Constructor to initialize EmailService.
     * @param emailService the service responsible for sending emails
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Sends a verification email to a specified recipient.
     * @param emailRequestDTO contains recipient's email, username, and a user token for verification
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
        try {
            emailService.sendVerificationEmail(emailRequestDTO.getTo(), emailRequestDTO.getUsername(), emailRequestDTO.getUserToken());
            return ResponseEntity.ok("Verification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification email.");
        }
    }

    /**
     * Sends a blacklist notification email to a specified recipient.
     * @param blacklistEmailDTO contains recipient's email, username, reason for blacklist, and ban duration
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/blacklists")
    public ResponseEntity<String> sendBlacklistEmail(@RequestBody @Valid BlacklistEmailDTO blacklistEmailDTO) {
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

    /**
     * Sends a whitelist notification email to a specified recipient.
     * @param whitelistEmailDTO contains recipient's email, username, and reason for whitelist
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/whitelists")
    public ResponseEntity<String> sendWhitelistEmail(@RequestBody @Valid WhitelistEmailDTO whitelistEmailDTO) {
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

    /**
     * Sends a password reset email to a specified recipient.
     * @param emailRequestDTO contains recipient's email, username, and a user token for password reset
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/password-recovery")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
        try {
            emailService.sendPasswordResetEmail(
                    emailRequestDTO.getTo(),
                    emailRequestDTO.getUsername(),
                    emailRequestDTO.getUserToken()
            );
            return ResponseEntity.ok("Password reset email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send password reset email.");
        }
    }

    /**
     * Sends a contact form feedback email.
     * @param contactFormDTO contains the sender's name, email, and message
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/feedback")
    public ResponseEntity<String> sendContactUsEmail(@RequestBody @Valid ContactFormDTO contactFormDTO) {
        try {
            emailService.sendContactUsEmail(
                    contactFormDTO.getName(),
                    contactFormDTO.getEmail(),
                    contactFormDTO.getMessage()
            );
            return ResponseEntity.ok("Feedback email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send feedback email.");
        }
    }

    /**
     * Sends a tournament notification email to a specified recipient.
     * @param notification contains recipient's email, subject, and message for the tournament notification
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/tournament-notification")
    public ResponseEntity<String> sendTournamentNotificationEmail(@RequestBody @Valid TournamentNotificationDTO notification) {
        try {
            emailService.sendTournamentNotificationEmail(notification.getTo(), notification.getSubject(), notification.getMessage());
            return ResponseEntity.ok("Tournament notification email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send tournament notification email.");
        }
    }

    /**
     * Sends a match reminder email to a specified recipient.
     * @param reminderEmailDTO contains recipient's email, subject, and message for the match reminder
     * @return ResponseEntity with a success message if email is sent, otherwise a failure message
     */
    @PostMapping("/matchreminder")
    public ResponseEntity<String> sendMatchReminderEmail(@RequestBody @Valid ReminderEmailDTO reminderEmailDTO) {
        try {
            emailService.sendMatchReminderEmail(reminderEmailDTO.getTo(), reminderEmailDTO.getTournamentName(), reminderEmailDTO.getScheduledTime());
            return ResponseEntity.ok("Match reminder email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send match reminder email.");
        }
    }

    /**
     * Health check endpoint to verify if the email service is running.
     * @return ResponseEntity containing a message indicating the service is up and running
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Service is up and running");
    }
}