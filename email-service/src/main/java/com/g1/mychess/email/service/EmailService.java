package com.g1.mychess.email.service;

import com.g1.mychess.email.dto.ReportEmailDTO;

import java.time.LocalDateTime;

/**
 * Interface for defining the contract for email services.
 * This interface outlines the methods for sending various types of emails, such as verification,
 * blacklist/whitelist notifications, password resets, contact feedback, and tournament notifications.
 *
 * Implementing classes are expected to provide the actual implementation for these email-sending actions.
 */
public interface EmailService {

    /**
     * Sends a verification email to the user with a verification token.
     *
     * @param to                The recipient's email address.
     * @param username          The username of the user receiving the verification email.
     * @param verificationToken The token used for email verification.
     */
    void sendVerificationEmail(String to, String username, String verificationToken);

    /**
     * Sends a blacklist notification email to the user with the reason and ban duration.
     *
     * @param to          The recipient's email address.
     * @param username    The username of the blacklisted user.
     * @param reason      The reason for the blacklist.
     * @param banDuration The duration of the ban in hours or days.
     */
    void sendBlacklistEmail(String to, String username, String reason, Long banDuration);

    /**
     * Sends a whitelist notification email to the user with the reason for being whitelisted.
     *
     * @param to       The recipient's email address.
     * @param username The username of the whitelisted user.
     * @param reason   The reason for the whitelist.
     */
    void sendWhitelistEmail(String to, String username, String reason);

    /**
     * Sends a password reset email to the user with a reset token.
     *
     * @param to         The recipient's email address.
     * @param username   The username of the user requesting the password reset.
     * @param resetToken The token used for password reset.
     */
    void sendPasswordResetEmail(String to, String username, String resetToken);

    /**
     * Sends a "Contact Us" email with the user's feedback.
     *
     * @param name    The name of the person submitting the feedback.
     * @param email   The email address of the person submitting the feedback.
     * @param message The feedback message submitted by the user.
     */
    void sendContactUsEmail(String name, String email, String message);

    /**
     * Sends a tournament notification email to the user with custom subject and message.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the tournament notification.
     * @param message The content of the tournament notification.
     */
    void sendTournamentNotificationEmail(String to, String subject, String message);

    void sendMatchReminderEmail(String to, String tournamentName, LocalDateTime scheduledTime);

    void sendPlayerReportEmail(ReportEmailDTO reportEmailDTO);
}