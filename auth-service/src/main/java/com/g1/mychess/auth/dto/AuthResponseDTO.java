package com.g1.mychess.auth.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class AuthResponseDTO {

    private String username;
    private List<GrantedAuthority> authorities;

    public AuthResponseDTO(String username, List<GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
