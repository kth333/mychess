package com.g1.mychess.email.util;

import java.time.LocalDateTime;

/**
 * Utility class that provides methods for building email content for various purposes, such as verification emails,
 * blacklist/whitelist notifications, password reset requests, and feedback/contact emails.
 */
public class EmailContentBuilder {

    /**
     * Builds the content for a verification email.
     *
     * @param username The username of the recipient of the email.
     * @param verificationToken The token used for email verification.
     * @param baseUrl The base URL to which the verification token should be appended.
     * @return A string containing the formatted verification email content.
     */
    public static String buildVerificationEmailContent(String username, String verificationToken, String baseUrl) {
        String verificationUrl = baseUrl + "/api/v1/auth/verification?token=" + verificationToken;
        return "Dear " + username + ",\n\n"
                + "Please verify your email address by clicking on the link below:\n"
                + verificationUrl + "\n\n"
                + "Thank you for using MyChess!";
    }

    /**
     * Builds the content for a blacklist notification email.
     *
     * @param username The username of the recipient of the email.
     * @param reason The reason for the blacklist.
     * @param banDuration The duration of the ban in hours.
     * @return A string containing the formatted blacklist notification email content.
     */
    public static String buildBlacklistEmailContent(String username, String reason, Long banDuration) {
        return "Dear " + username + ",\n\n"
                + "You have been banned from participating in tournaments for the following reason: " + reason + "\n"
                + "Ban Duration: " + banDuration + " hours.\n\n"
                + "If you believe this is a mistake, please contact support.\n\n"
                + "Thank you,\nMyChess Team";
    }

    /**
     * Builds the content for a whitelist notification email.
     *
     * @param username The username of the recipient of the email.
     * @param reason The reason for being whitelisted.
     * @return A string containing the formatted whitelist notification email content.
     */
    public static String buildWhitelistEmailContent(String username, String reason) {
        return "Dear " + username + ",\n\n"
                + "You have been whitelisted and can now participate in tournaments again.\n"
                + "Reason: " + reason + "\n\n"
                + "Thank you,\nMyChess Team";
    }

    /**
     * Builds the content for a "Contact Us" feedback email.
     *
     * @param name The name of the person submitting feedback.
     * @param email The email address of the person submitting feedback.
     * @param message The content of the feedback message.
     * @return A string containing the formatted "Contact Us" email content.
     */
    public static String buildContactUsEmailContent(String name, String email, String message) {
        return "Feedback from: " + name + ",\n\n"
                + "Email Address: " + email + ",\n\n"
                + "Message: " + message;
    }

    /**
     * Builds the content for a password reset email.
     *
     * @param username The username of the recipient of the email.
     * @param resetToken The token used for password reset.
     * @param baseUrl The base URL to which the reset token should be appended.
     * @return A string containing the formatted password reset email content.
     */
    public static String buildPasswordResetEmailContent(String username, String resetToken, String baseUrl) {
        String resetUrl = baseUrl + "/password-reset/" + resetToken;
        return "Dear " + username + ",\n\n"
                + "We received a request to reset your password. You can reset your password by clicking on the link below:\n"
                + resetUrl + "\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Thank you,\nMyChess Team";
    }

    public static String buildMatchReminderEmailContent(String tournamentName, LocalDateTime scheduledTime) {
        return "Dear Player,\n\n"
                + "This is a reminder for your upcoming match (Tournament: " + tournamentName + ") scheduled for " + scheduledTime + ".\n\n"
                + "Please be ready and good luck!\n\n"
                + "Thank you,\nMyChess Team";
    }
}