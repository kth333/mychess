package com.g1.mychess.player.dto;

import java.time.LocalDate;

public class RegisterRequestDTO {

    private String username;
    private String password;
    private String email;
    private String gender;
    private String country;
    private String region;
    private String city;
    private LocalDate birthDate;

    // Default constructor
    public RegisterRequestDTO() {
    }

    // Parameterized constructor
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
