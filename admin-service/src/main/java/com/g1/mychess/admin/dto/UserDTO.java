package com.g1.mychess.admin.dto;

/**
 * Data Transfer Object (DTO) representing a user, containing information such as
 * user ID, username, password, email, and role. This class is used for transferring
 * user-related data between layers of the application.
 */
public class UserDTO {

    private Long userId;      // Unique identifier for the user
    private String username;  // Username of the user
    private String password;  // Password of the user (usually hashed)
    private String email;     // Email address of the user
    private String role;      // Role assigned to the user (e.g., "ROLE_ADMIN")

    // Constructors, Getters, and Setters

    /**
     * Default constructor for creating an empty UserDTO object.
     */
    public UserDTO() {}

    /**
     * Constructor to initialize a UserDTO with user ID, username, password, email, and role.
     *
     * @param userId the unique ID of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param email the email address of the user
     * @param role the role assigned to the user (e.g., "ROLE_ADMIN")
     */
    public UserDTO(Long userId, String username, String password, String email, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /**
     * Gets the unique user ID.
     *
     * @return the ID of the user
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Sets the user ID.
     *
     * @param userId the ID to set for the user
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set for the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email to set for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the role assigned to the user.
     *
     * @return the role of the user (e.g., "ROLE_ADMIN")
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the role to set for the user (e.g., "ROLE_ADMIN")
     */
    public void setRole(String role) {
        this.role = role;
    }
}