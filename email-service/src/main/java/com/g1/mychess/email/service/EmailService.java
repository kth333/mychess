package com.g1.mychess.email.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String username, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification - MyChess");

        String verificationUrl = authServiceUrl + "/api/v1/auth/verify-email?token=";
        String emailContent = "Dear " + username + ",\n\n"
                + "Please verify your email address by clicking on the link below:\n"
                + verificationUrl + verificationToken + "\n\n"
                + "Thank you for using MyChess!";

        message.setText(emailContent);
        mailSender.send(message);
    }

    public void sendBlacklistEmail(String to, String username, String reason, Long banDuration) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Blacklist Notification - MyChess");

        String emailContent = "Dear " + username + ",\n\n"
                + "You have been banned from participating in tournaments for the following reason: " + reason + "\n"
                + "Ban Duration: " + banDuration + " hours.\n\n"
                + "If you believe this is a mistake, please contact support.\n\n"
                + "Thank you,\nMyChess Team";

        message.setText(emailContent);
        mailSender.send(message);
    }

    public void sendWhitelistEmail(String to, String username, String reason) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Whitelist Notification - MyChess");

        String emailContent = "Dear " + username + ",\n\n"
                + "You have been whitelisted and can now participate in tournaments again.\n"
                + "Reason: " + reason + "\n\n"
                + "Thank you,\nMyChess Team";

        message.setText(emailContent);
        mailSender.send(message);
    }
}
