package com.g1.mychess.tournament.model;

import com.g1.mychess.enums.Gender;
import com.g1.mychess.enums.TournamentFormat;
import com.g1.mychess.user.model.Admin;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "registration_start_date", nullable = false)
    private LocalDateTime registrationStartDate;

    @Column(name = "registration_end_date", nullable = false)
    private LocalDateTime registrationEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    private TournamentFormat format;

    @Embedded
    private TimeControlSetting timeControlSetting;

    @Enumerated(EnumType.STRING)
    @Column(name = "time_control_type", nullable = false)
    private TimeControlSetting.TimeControlType timeControlType;

    @PrePersist
    @PreUpdate
    public void updateTimeControlType() {
        this.timeControlType = timeControlSetting.getTimeControlType();
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TournamentStatus status;

    @Column(name = "min_rating")
    private Integer minRating;

    @Column(name = "max_rating")
    private Integer maxRating;

    @Column(name = "affects_elo", nullable = false)
    private boolean affectsElo;

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "max_age")
    private Integer maxAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "required_gender")
    private Gender requiredGender;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TournamentPlayer> participants;

    public enum TournamentStatus {
        UPCOMING,
        ONGOING,
        COMPLETED,
        CANCELED,
        PAUSED
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getRegistrationStartDate() {
        return registrationStartDate;
    }

    public void setRegistrationStartDate(LocalDateTime registrationStartDate) { this.registrationStartDate = registrationStartDate; }

    public LocalDateTime getRegistrationEndDate() {
        return registrationEndDate;
    }

    public void setRegistrationEndDate(LocalDateTime registrationEndDate) { this.registrationEndDate = registrationEndDate; }

    public TournamentFormat getFormat() {
        return format;
    }

    public void setFormat(TournamentFormat format) {
        this.format = format;
    }

    public TimeControlSetting getTimeControlSetting() { return timeControlSetting; }

    public void setTimeControlSetting(TimeControlSetting timeControlSetting) { this.timeControlSetting = timeControlSetting; }

    public TimeControlSetting.TimeControlType getTimeControlType() { return timeControlType; }

    public void setTimeControlType(TimeControlSetting.TimeControlType timeControlType) { this.timeControlType = timeControlType; }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) { this.status = status; }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public Integer getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Integer maxRating) {
        this.maxRating = maxRating;
    }

    public boolean isAffectsElo() {
        return affectsElo;
    }

    public void setAffectsElo(boolean affectsElo) {
        this.affectsElo = affectsElo;
    }

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public Gender getRequiredGender() {
        return requiredGender;
    }

    public void setRequiredGender(Gender requiredGender) {
        this.requiredGender = requiredGender;
    }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Set<TournamentPlayer> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<TournamentPlayer> participants) {
        this.participants = participants;
    }
}
