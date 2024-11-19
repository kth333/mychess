package com.g1.mychess.player.dto;

/**
 * Data Transfer Object (DTO) class that represents user information in the system.
 * This class contains user details such as user ID, username, password, email, and role.
 */
public class UserDTO {

    private Long userId;
    private String username;
    private String password;
    private String email;
    private String role;

    /**
     * Default constructor for UserDTO.
     * Initializes a new instance of the UserDTO class with default values.
     */
    public UserDTO() {}

    /**
     * Constructs a new UserDTO with the specified values for user ID, username, password, email, and role.
     *
     * @param userId the user's ID
     * @param username the user's username
     * @param password the user's password
     * @param email the user's email address
     * @param role the user's role
     */
    public UserDTO(Long userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the user ID.
     *
     * @return the user ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the user ID to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return the user's role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
}
