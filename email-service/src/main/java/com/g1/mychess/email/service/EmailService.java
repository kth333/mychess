package com.g1.mychess.email.service;

public interface EmailService {

    void sendVerificationEmail(String to, String username, String verificationToken);

    void sendBlacklistEmail(String to, String username, String reason, Long banDuration);

    void sendWhitelistEmail(String to, String username, String reason);

    void sendPasswordResetEmail(String to, String username, String resetToken);

    void sendContactUsEmail(String name, String email, String message);
}