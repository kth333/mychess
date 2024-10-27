package com.g1.mychess.email.service.impl;

import com.g1.mychess.email.service.EmailService;
import com.g1.mychess.email.util.EmailContentBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${mychess.verification.url}")
    private String verificationUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        String subject = "Email Verification - MyChess";
        String content = EmailContentBuilder.buildVerificationEmailContent(username, verificationToken, getVerificationUrl());
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

    private void sendEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    private String getVerificationUrl() {
        return StringUtils.hasText(verificationUrl) ? verificationUrl : "http://localhost:8080/api/v1/auth/verify-email?token=";
    }
}