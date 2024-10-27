package com.g1.mychess.player.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerProfileUpdateDTO {

    private String fullName;
    private String bio;
    private String avatarUrl;
    private String country;
    private String region;
    private String city;

    @JsonProperty("isPublic")
    private boolean isPublic;

    public PlayerProfileUpdateDTO() {}

    public PlayerProfileUpdateDTO(String fullName, String bio, String avatarUrl, String country, String region, String city, boolean isPublic) {
        this.fullName = fullName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.region = region;
        this.city = city;
        this.isPublic = isPublic;
    }

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}