package com.g1.mychess.user.dto;

public class UserCreationResponseDTO {
    private Long userId;
    private String message;

    // Constructor, Getters, and Setters
    public UserCreationResponseDTO(Long userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public Long getId() {
        return userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
