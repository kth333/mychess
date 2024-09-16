package com.g1.mychess.player.dto;

public class AdminPlayerDTO {

    private Long id;
    private boolean isBlacklisted;
    private String username;
    private String email;

    // Constructors, Getters, Setters

    public AdminPlayerDTO(Long id, boolean isBlacklisted, String username, String email) {
        this.id = id;
        this.isBlacklisted = isBlacklisted;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
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
}