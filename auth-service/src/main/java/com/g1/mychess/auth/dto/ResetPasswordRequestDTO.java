package com.g1.mychess.auth.dto;

public class ResetPasswordRequestDTO {
    private String resetToken;
    private String newPassword;

    // Constructors, getters, and setters
    public ResetPasswordRequestDTO() {}

    public ResetPasswordRequestDTO(String resetToken, String newPassword) {
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
