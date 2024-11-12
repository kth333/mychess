package com.g1.mychess.email.service.impl;

import com.g1.mychess.email.dto.ReportEmailDTO;
import com.g1.mychess.email.service.EmailService;
import com.g1.mychess.email.util.EmailContentBuilder;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link EmailService} interface.
 *
 * This service is responsible for sending various types of emails such as verification emails,
 * blacklist/whitelist notifications, password reset requests, contact us feedback, and tournament notifications.
 * It uses {@link JavaMailSender} to send emails and {@link EmailContentBuilder} to construct the email content.
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private final JavaMailSender mailSender;

    // URL for the authentication service, injected from application properties
    @Value("${auth.service.url}")
    private String authServiceUrl;

    // URL for the frontend service, injected from application properties
    @Value("${frontend.url}")
    private String frontendUrl;

    // Email address where feedback is received, injected from application properties
    @Value("${email.feedback.address}")
    private String feedbackEmailAddress;

    /**
     * Constructor to inject the {@link JavaMailSender} dependency.
     *
     * @param mailSender The mail sender used to send the emails.
     */
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a verification email to the user with a verification token.
     *
     * @param to The recipient's email address.
     * @param username The username of the user receiving the verification email.
     * @param verificationToken The token used for email verification.
     */
    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        String subject = "Email Verification - MyChess";
        String content = EmailContentBuilder.buildVerificationEmailContent(username, verificationToken, authServiceUrl);
        sendEmail(to, subject, content);
    }

    /**
     * Sends a blacklist notification email to the user with the reason and ban duration.
     *
     * @param to The recipient's email address.
     * @param username The username of the blacklisted user.
     * @param reason The reason for the blacklist.
     * @param banDuration The duration of the ban in hours or days.
     */
    @Override
    public void sendBlacklistEmail(String to, String username, String reason, Long banDuration) {
        String subject = "Blacklist Notification - MyChess";
        String content = EmailContentBuilder.buildBlacklistEmailContent(username, reason, banDuration);
        sendEmail(to, subject, content);
    }

    /**
     * Sends a whitelist notification email to the user with the reason for being whitelisted.
     *
     * @param to The recipient's email address.
     * @param username The username of the whitelisted user.
     * @param reason The reason for the whitelist.
     */
    @Override
    public void sendWhitelistEmail(String to, String username, String reason) {
        String subject = "Whitelist Notification - MyChess";
        String content = EmailContentBuilder.buildWhitelistEmailContent(username, reason);
        sendEmail(to, subject, content);
    }

    /**
     * Sends a password reset email to the user with a reset token.
     *
     * @param to The recipient's email address.
     * @param username The username of the user requesting the password reset.
     * @param resetToken The token used for password reset.
     */
    @Override
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        String subject = "Password Reset Request - MyChess";
        String content = EmailContentBuilder.buildPasswordResetEmailContent(username, resetToken, frontendUrl);
        sendEmail(to, subject, content);
    }

    /**
     * Sends a "Contact Us" email with the user's feedback.
     *
     * @param name The name of the person submitting the feedback.
     * @param email The email address of the person submitting the feedback.
     * @param message The feedback message submitted by the user.
     */
    @Override
    public void sendContactUsEmail(String name, String email, String message) {
        String subject = "Feedback from " + name;
        String content = EmailContentBuilder.buildContactUsEmailContent(name, email, message);
        sendEmail(feedbackEmailAddress, subject, content);
    }

    /**
     * Sends a tournament notification email to the user with custom subject and message.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the tournament notification.
     * @param message The content of the tournament notification.
     */
    @Override
    public void sendTournamentNotificationEmail(String to, String subject, String message) {
        sendEmail(to, subject, message);
    }

    @Override
    public void sendMatchReminderEmail(String to, String tournamentName, LocalDateTime scheduledTime) {
        String subject = "MyChess - Match Reminder for Tournament:" + tournamentName;
        String content = EmailContentBuilder.buildMatchReminderEmailContent(tournamentName, scheduledTime);
        sendEmail(to, subject, content);
    }

    @Override
    public void sendPlayerReportEmail(ReportEmailDTO reportEmailDTO) {
        String subject = "Player Report: " + reportEmailDTO.getReportedPlayerUsername();
        String content = EmailContentBuilder.buildPlayerReportEmailContent(
                reportEmailDTO.getReporterUsername(),
                reportEmailDTO.getReportedPlayerUsername(),
                reportEmailDTO.getReason(),
                reportEmailDTO.getDescription()
        );
        sendEmail(feedbackEmailAddress, subject, content);
    }

    /**
     * Helper method to send an email using the {@link JavaMailSender}.
     *
     * @param to The recipient's email address.
     * @param subject The subject of the email.
     * @param content The content/body of the email.
     */
    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}