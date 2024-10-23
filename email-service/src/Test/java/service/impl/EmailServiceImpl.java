package service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String to, String username, String verificationToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Email Verification - MyChess");

        String verificationUrl = "localhost:8080/api/v1/auth/verify-email?token=";
        String emailContent = "Dear " + username + ",\n\n"
                + "Please verify your email address by clicking on the link below:\n"
                + verificationUrl + verificationToken + "\n\n"
                + "Thank you for using MyChess!";

        message.setText(emailContent);
        mailSender.send(message);
    }

    @Override
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

    @Override
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