package com.g1.mychess.location.model;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    public Location(String city, String region, String country) {
        this.city = city;
        this.region = region;
        this.country = country;
        generateName();
    }

    public Location() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void generateName() {
        StringBuilder fullName = new StringBuilder();

        if (city != null && !city.isEmpty()) {
            fullName.append(city);
        }

        if (region != null && !region.isEmpty()) {
            if (!fullName.isEmpty()) {
                fullName.append(", ");
            }
            fullName.append(region);
        }

        if (country != null && !country.isEmpty()) {
            if (!fullName.isEmpty()) {
                fullName.append(", ");
            }
            fullName.append(country);
        }

        this.name = fullName.toString();
    }

    @PrePersist
    @PreUpdate
    private void prePersistOrUpdate() {
        generateName();
    }
}
