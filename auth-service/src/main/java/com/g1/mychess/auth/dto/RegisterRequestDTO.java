package com.g1.mychess.auth.dto;

import com.g1.mychess.user.model.User;

public class RegisterRequestDTO {
    
    private String username;
    private String password;
    private String email;
    private User.Role role;

    // Default constructor
    public RegisterRequestDTO() {
    }

    // Parameterized constructor
    public RegisterRequestDTO(String username, String password, String email, User.Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }
}

