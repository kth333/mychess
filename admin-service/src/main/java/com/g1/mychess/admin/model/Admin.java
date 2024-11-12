package com.g1.mychess.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Represents an administrator (admin) in the system.
 * This entity is used to manage the admins who have control over the application,
 * including their credentials (username, email, password) and role.
 */
@Entity
@Table(name = "admins")
public class Admin {

    /**
     * The unique ID of the admin.
     * This is the primary key of the `admins` table.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    /**
     * The role assigned to the admin.
     * Default value is `ROLE_ADMIN`.
     * This is used for role-based access control (RBAC).
     */
    @Column(name = "role")
    @NotNull
    private String role = "ROLE_ADMIN";

    /**
     * The username of the admin.
     * This is a required field and must be unique across the application.
     * The maximum length is limited to 50 characters.
     */
    @NotNull(message = "Username cannot be empty.")
    @Size(max = 50, message = "Username cannot exceed 50 characters.")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * The email address of the admin.
     * This is a required field and must be a valid email address.
     * The email is also unique within the application.
     */
    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email should be valid.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The password of the admin.
     * This is a required field and must be at least 8 characters long.
     */
    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @Column(name = "password", nullable = false)
    private String password;

    // Getter and Setter Methods

    /**
     * Gets the unique ID of the admin.
     * @return the admin ID
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * Sets the unique ID of the admin.
     * @param adminId the admin ID to set
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * Gets the role assigned to the admin.
     * @return the admin role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role for the admin.
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the username of the admin.
     * @return the admin's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the admin.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the admin.
     * @return the admin's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the admin.
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the admin.
     * @return the admin's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the admin.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}