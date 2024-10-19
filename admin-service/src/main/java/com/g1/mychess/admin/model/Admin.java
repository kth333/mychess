package com.g1.mychess.admin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "role")
    @NotNull
    private String role = "ROLE_ADMIN";

    @NotNull(message = "Username cannot be empty")
    @Size(max = 50, message = "Username cannot exceed 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    // Getters and Setters

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}