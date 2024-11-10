package com.g1.mychess.auth.dto;

/**
 * Data Transfer Object representing a request to reset a user's password.
 * It includes the reset token and the new password for the user.
 */
public class ResetPasswordRequestDTO {

    /**
     * The token used to authenticate the password reset request.
     */
    private String resetToken;

    /**
     * The new password chosen by the user.
     */
    private String newPassword;

    /**
     * Default constructor for ResetPasswordRequestDTO.
     * Initializes a new instance without setting any fields.
     */
    public ResetPasswordRequestDTO() {}

    /**
     * Constructs a ResetPasswordRequestDTO with the provided reset token and new password.
     *
     * @param resetToken the token to authenticate the password reset request
     * @param newPassword the new password chosen by the user
     */
    public ResetPasswordRequestDTO(String resetToken, String newPassword) {
        this.resetToken = resetToken;
        this.newPassword = newPassword;
    }

    /**
     * Gets the reset token used for password reset authentication.
     *
     * @return the reset token
     */
    public String getResetToken() {
        return resetToken;
    }

    /**
     * Sets the reset token for password reset authentication.
     *
     * @param resetToken the reset token to set
     */
    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    /**
     * Gets the new password chosen by the user.
     *
     * @return the new password
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password for the user.
     *
     * @param newPassword the new password to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
