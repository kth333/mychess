package com.g1.mychess.email.service.impl;

import com.g1.mychess.email.service.EmailService;
import com.g1.mychess.email.util.EmailContentBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Value("${frontend.url}")
    private String frontendUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        String subject = "Email Verification - MyChess";
        String content = EmailContentBuilder.buildVerificationEmailContent(username, verificationToken, authServiceUrl);
        sendEmail(to, subject, content);
    }

    @Override
    public void sendBlacklistEmail(String to, String username, String reason, Long banDuration) {
        String subject = "Blacklist Notification - MyChess";
        String content = EmailContentBuilder.buildBlacklistEmailContent(username, reason, banDuration);
        sendEmail(to, subject, content);
    }

    @Override
    public void sendWhitelistEmail(String to, String username, String reason) {
        String subject = "Whitelist Notification - MyChess";
        String content = EmailContentBuilder.buildWhitelistEmailContent(username, reason);
        sendEmail(to, subject, content);
    }

    @Override
    public void sendPasswordResetEmail(String to, String username, String resetToken) {
        String subject = "Password Reset Request - MyChess";
        String content = EmailContentBuilder.buildPasswordResetEmailContent(username, resetToken, frontendUrl);
        sendEmail(to, subject, content);
    }

    @Override
    public void sendContactUsEmail(String name, String email, String message) {
        String subject = "Feedback from " + name;
        String content = EmailContentBuilder.buildContactUsEmailContent(name, email, message);
        sendEmail("mychessfeedback@gmail.com", subject, content);
    }

    @Override
    public void sendTournamentNotificationEmail(String to, String subject, String message) {
        sendEmail(to, subject, message);
    }

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }
}