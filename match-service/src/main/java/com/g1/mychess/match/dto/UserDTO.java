package com.g1.mychess.match.dto;

/**
 * Data Transfer Object (DTO) representing a user.
 * Contains fields for user information such as userId, username, password, email, and role.
 */
public class UserDTO {

    /**
     * The unique identifier of the user.
     */
    private Long userId;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The role assigned to the user (e.g., ADMIN, PLAYER).
     */
    private String role;

    // Constructors, Getters, and Setters

    /**
     * Default constructor for creating an empty UserDTO.
     */
    public UserDTO() {}

    /**
     * Constructor for creating a UserDTO with specified values.
     *
     * @param userId The unique identifier of the user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param email The email address of the user.
     * @param role The role of the user.
     */
    public UserDTO(Long userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the userId of the user.
     *
     * @return The unique identifier of the user.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the userId of the user.
     *
     * @param userId The unique identifier of the user.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role assigned to the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role of the user.
     */
    public void setRole(String role) {
        this.role = role;
    }
}