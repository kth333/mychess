package com.g1.mychess.player.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) class for user registration request.
 * This class contains the necessary information provided by a user during registration.
 */
public class RegisterRequestDTO {

    private String username;
    private String password;
    private String email;
    private String gender;
    private String country;
    private String region;
    private String city;
    private LocalDate birthDate;

    /**
     * Default constructor for RegisterRequestDTO.
     */
    public RegisterRequestDTO() {
    }

    /**
     * Constructs a RegisterRequestDTO with the provided parameters.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @param email the email of the player
     * @param gender the gender of the player
     * @param country the country where the player resides
     * @param region the region within the country
     * @param city the city where the player resides
     * @param birthDate the birth date of the player
     */
    public RegisterRequestDTO(String username, String password, String email, String gender, String country, String region, String city, LocalDate birthDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.country = country;
        this.region = region;
        this.city = city;
        this.birthDate = birthDate;
    }

    /**
     * Gets the username of the player.
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the player.
     *
     * @param username the username of the player
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the player.
     *
     * @return the password of the player
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the player.
     *
     * @param password the password of the player
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the player.
     *
     * @return the email of the player
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the player.
     *
     * @param email the email of the player
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the gender of the player.
     *
     * @return the gender of the player
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the player.
     *
     * @param gender the gender of the player
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the country where the player resides.
     *
     * @return the country of the player
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country where the player resides.
     *
     * @param country the country of the player
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the region where the player resides.
     *
     * @return the region of the player
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region where the player resides.
     *
     * @param region the region of the player
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the city where the player resides.
     *
     * @return the city of the player
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city where the player resides.
     *
     * @param city the city of the player
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the birth date of the player.
     *
     * @return the birth date of the player
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the player.
     *
     * @param birthDate the birth date of the player
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
