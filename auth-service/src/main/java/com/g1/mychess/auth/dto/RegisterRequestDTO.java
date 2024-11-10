package com.g1.mychess.auth.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object representing the details required for user registration,
 * including username, password, email, and other personal information.
 */
public class RegisterRequestDTO {

    /**
     * The username chosen by the user during registration.
     */
    private String username;

    /**
     * The password chosen by the user for the registration.
     */
    private String password;

    /**
     * The email address provided by the user for registration and communication.
     */
    private String email;

    /**
     * The gender of the user.
     */
    private String gender;

    /**
     * The country where the user resides.
     */
    private String country;

    /**
     * The region (state or province) where the user resides.
     */
    private String region;

    /**
     * The city where the user resides.
     */
    private String city;

    /**
     * The birth date of the user.
     */
    private LocalDate birthDate;

    /**
     * Default constructor for RegisterRequestDTO.
     * Initializes a new instance without setting any fields.
     */
    public RegisterRequestDTO() {
    }

    /**
     * Constructs a RegisterRequestDTO with the provided registration details.
     *
     * @param username the username chosen by the user
     * @param password the password chosen by the user
     * @param email the email address of the user
     * @param gender the gender of the user
     * @param country the country where the user resides
     * @param region the region (state/province) where the user resides
     * @param city the city where the user resides
     * @param birthDate the birth date of the user
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
     * Gets the username of the user.
     *
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the gender of the user.
     *
     * @return the gender of the user
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the country of the user.
     *
     * @return the country of the user
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the user.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the region (state or province) of the user.
     *
     * @return the region of the user
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region (state or province) of the user.
     *
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the city of the user.
     *
     * @return the city of the user
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the user.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the birth date of the user.
     *
     * @return the birth date of the user
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the user.
     *
     * @param birthDate the birth date to set
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
