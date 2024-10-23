package service;

public interface EmailService {

    void sendVerificationEmail(String to, String username, String verificationToken);

    void sendBlacklistEmail(String to, String username, String reason, Long banDuration);

    void sendWhitelistEmail(String to, String username, String reason);
}