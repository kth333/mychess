package com.g1.mychess.player.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object (DTO) class that represents a player's profile update information.
 * This class is used for updating a player's profile details such as their full name, bio, avatar URL,
 * location (country, region, city), and the visibility of the profile.
 */
public class PlayerProfileUpdateDTO {

    private String fullName;
    private String bio;
    private String avatarUrl;
    private String country;
    private String region;
    private String city;

    @JsonProperty("isPublic")
    private boolean isPublic;

    /**
     * Default constructor for PlayerProfileUpdateDTO.
     */
    public PlayerProfileUpdateDTO() {}

    /**
     * Constructs a PlayerProfileUpdateDTO with the provided parameters.
     *
     * @param fullName the full name of the player
     * @param bio the biography or description of the player
     * @param avatarUrl the URL of the player's avatar image
     * @param country the country of the player
     * @param region the region of the player
     * @param city the city of the player
     * @param isPublic whether the player's profile is public
     */
    public PlayerProfileUpdateDTO(String fullName, String bio, String avatarUrl, String country, String region, String city, boolean isPublic) {
        this.fullName = fullName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.region = region;
        this.city = city;
        this.isPublic = isPublic;
    }

    /**
     * Gets the full name of the player.
     *
     * @return the full name of the player
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the player.
     *
     * @param fullName the full name of the player
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the biography of the player.
     *
     * @return the bio of the player
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the biography of the player.
     *
     * @param bio the biography of the player
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the avatar URL of the player.
     *
     * @return the avatar URL of the player
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the avatar URL of the player.
     *
     * @param avatarUrl the avatar URL of the player
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Gets the country of the player.
     *
     * @return the country of the player
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the player.
     *
     * @param country the country of the player
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the region of the player.
     *
     * @return the region of the player
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region of the player.
     *
     * @param region the region of the player
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the city of the player.
     *
     * @return the city of the player
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the player.
     *
     * @param city the city of the player
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets whether the player's profile is public.
     *
     * @return true if the profile is public, false otherwise
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets whether the player's profile is public.
     *
     * @param isPublic true if the profile should be public, false otherwise
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
