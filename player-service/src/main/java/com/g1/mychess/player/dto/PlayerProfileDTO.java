package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.g1.mychess.player.model.CustomChessRank;

/**
 * Data Transfer Object (DTO) representing a player's profile details.
 * This class encapsulates the player's information such as personal details, chess ratings,
 * total wins/losses/draws, and the visibility of their profile.
 */
public class PlayerProfileDTO {

    private Long playerId;
    private String username;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String gender;
    private String country;
    private String region;
    private String city;
    private LocalDate birthDate;
    private CustomChessRank rank;
    private double glickoRating;
    private double ratingDeviation;
    private double volatility;
    private int totalWins;
    private int totalLosses;
    private int totalDraws;

    @JsonProperty("isPublic")
    private boolean isPublic;
    private int age;

    /**
     * Constructs a PlayerProfileDTO with all necessary profile details.
     *
     * @param playerId the unique identifier for the player
     * @param username the username of the player
     * @param fullName the full name of the player
     * @param bio the biography or description of the player
     * @param avatarUrl the URL of the player's avatar image
     * @param gender the gender of the player
     * @param country the country of the player
     * @param region the region of the player
     * @param city the city of the player
     * @param birthDate the birth date of the player
     * @param rank the custom chess rank of the player
     * @param glickoRating the Glicko rating of the player
     * @param ratingDeviation the Glicko rating deviation of the player
     * @param volatility the volatility of the player's Glicko rating
     * @param totalWins the total number of wins for the player
     * @param totalLosses the total number of losses for the player
     * @param totalDraws the total number of draws for the player
     * @param isPublic whether the player's profile is public
     * @param age the age of the player
     */
    public PlayerProfileDTO(Long playerId, String username, String fullName, String bio, String avatarUrl, String gender, String country, String region, String city, LocalDate birthDate, CustomChessRank rank, double glickoRating, double ratingDeviation, double volatility, int totalWins, int totalLosses, int totalDraws, boolean isPublic, int age) {
        this.playerId = playerId;
        this.username = username;
        this.fullName = fullName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.country = country;
        this.region = region;
        this.city = city;
        this.birthDate = birthDate;
        this.rank = rank;
        this.glickoRating = glickoRating;
        this.ratingDeviation = ratingDeviation;
        this.volatility = volatility;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalDraws = totalDraws;
        this.isPublic = isPublic;
        this.age = age;
    }

    // Getters and Setters

    /**
     * Gets the player's unique identifier.
     *
     * @return the playerId
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerId the playerId to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player's username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the player's full name.
     *
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the player's full name.
     *
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the player's biography.
     *
     * @return the bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the player's biography.
     *
     * @param bio the bio to set
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the player's avatar URL.
     *
     * @return the avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the player's avatar URL.
     *
     * @param avatarUrl the avatarUrl to set
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Gets the player's gender.
     *
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the player's gender.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the player's country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the player's country.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the player's region.
     *
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the player's region.
     *
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the player's city.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the player's city.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the player's birth date.
     *
     * @return the birthDate
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the player's birth date.
     *
     * @param birthDate the birthDate to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the player's custom chess rank.
     *
     * @return the rank
     */
    public CustomChessRank getRank() {
        return rank;
    }

    /**
     * Sets the player's custom chess rank.
     *
     * @param rank the rank to set
     */
    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    /**
     * Gets the player's Glicko rating.
     *
     * @return the glickoRating
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the player's Glicko rating.
     *
     * @param glickoRating the glickoRating to set
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the player's rating deviation.
     *
     * @return the ratingDeviation
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the player's rating deviation.
     *
     * @param ratingDeviation the ratingDeviation to set
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the player's volatility.
     *
     * @return the volatility
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the player's volatility.
     *
     * @param volatility the volatility to set
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    /**
     * Gets the total number of wins for the player.
     *
     * @return the totalWins
     */
    public int getTotalWins() {
        return totalWins;
    }

    /**
     * Sets the total number of wins for the player.
     *
     * @param totalWins the totalWins to set
     */
    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    /**
     * Gets the total number of losses for the player.
     *
     * @return the totalLosses
     */
    public int getTotalLosses() {
        return totalLosses;
    }

    /**
     * Sets the total number of losses for the player.
     *
     * @param totalLosses the totalLosses to set
     */
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    /**
     * Gets the total number of draws for the player.
     *
     * @return the totalDraws
     */
    public int getTotalDraws() {
        return totalDraws;
    }

    /**
     * Sets the total number of draws for the player.
     *
     * @param totalDraws the totalDraws to set
     */
    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
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
     * @param isPublic true to make the profile public, false otherwise
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Gets the player's age.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the player's age.
     *
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }
}
