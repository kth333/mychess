package com.g1.mychess.email.service;

import org.springframework.beans.factory.annotation.Autowired;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String username, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification - MyChess");

        String verificationUrl = "http://localhost:8080/api/v1/auth/verify-email?token=";
        String emailContent = "Dear " + username + ",\n\n"
                + "Please verify your email address by clicking on the link below:\n"
                + verificationUrl + verificationToken + "\n\n"
                + "Thank you for using MyChess!";

        message.setText(emailContent);
        mailSender.send(message);
    }
}
