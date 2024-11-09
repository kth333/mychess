package com.g1.mychess.email.util;

public class EmailContentBuilder {

    public static String buildVerificationEmailContent(String username, String verificationToken, String baseUrl) {
        String verificationUrl = baseUrl + "/api/v1/auth/verify-email?token=" + verificationToken;
        return "Dear " + username + ",\n\n"
                + "Please verify your email address by clicking on the link below:\n"
                + verificationUrl + "\n\n"
                + "Thank you for using MyChess!";
    }

    public static String buildBlacklistEmailContent(String username, String reason, Long banDuration) {
        return "Dear " + username + ",\n\n"
                + "You have been banned from participating in tournaments for the following reason: " + reason + "\n"
                + "Ban Duration: " + banDuration + " hours.\n\n"
                + "If you believe this is a mistake, please contact support.\n\n"
                + "Thank you,\nMyChess Team";
    }

    public static String buildWhitelistEmailContent(String username, String reason) {
        return "Dear " + username + ",\n\n"
                + "You have been whitelisted and can now participate in tournaments again.\n"
                + "Reason: " + reason + "\n\n"
                + "Thank you,\nMyChess Team";
    }
}