package com.g1.mychess.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tournament_id")
    private Long id;

    @Column(name = "admin_id", nullable = false)
    @NotNull
    private Long adminId;

    @Column(name = "name", nullable = false, unique = true)
    @NotNull
    @Size(min = 1, max = 100, message = "Tournament name cannot exceed 100 characters.")
    private String name;

    @Column(name = "description", nullable = false)
    @NotNull
    @Size(max = 500, message = "Tournament description cannot exceed 500 characters.")
    private String description;

    @Column(name = "max_players")
    @Min(value = 2, message = "At least 2 players are required to start the tournament.")
    private Integer maxPlayers;

    @Column(name = "start_date_time", nullable = false)
    @NotNull
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    @NotNull
    private LocalDateTime endDateTime;

    @Column(name = "registration_start_date", nullable = false)
    @NotNull
    private LocalDateTime registrationStartDate;

    @Column(name = "registration_end_date", nullable = false)
    @NotNull
    private LocalDateTime registrationEndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "format", nullable = false)
    @NotNull
    private TournamentFormat format;

    @Embedded
    private TimeControlSetting timeControlSetting;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    private TournamentStatus status;

    @Column(name = "min_rating")
    @Min(value = 0, message = "Minimum rating cannot go lower than 0.")
    private Integer minRating;

    @Column(name = "max_rating")
    @Min(value = 0, message = "Maximum rating cannot go lower than 0.")
    private Integer maxRating;

    @Column(name = "affects_rating", nullable = false)
    private boolean affectsRating;

    @Column(name = "min_age")
    @Min(value = 0, message = "Minimum age cannot go lower than 0.")
    @NotNull
    private Integer minAge;

    @Column(name = "max_age")
    @Min(value = 0, message = "Maximum age cannot go lower than 0.")
    @NotNull
    private Integer maxAge;

    @Column(name = "required_gender")
    private String requiredGender;

    @Column(name = "country")
    @NotNull
    private String country;

    @Column(name = "region")
    @NotNull
    private String region;

    @Column(name = "city")
    @Size(max = 50)
    private String city;

    @Column(name = "address", nullable = false)
    @NotNull
    @Size(max = 255)  // Assuming max length for address is 255 characters
    private String address;

    @Column(name = "current_round")
    @Min(1)
    private int currentRound;

    @Column(name = "max_rounds", nullable = false)
    @NotNull
    @Min(1)
    private int maxRounds;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TournamentPlayer> participants = new HashSet<>();

    public TimeControlSetting.TimeControlType getTimeControlType() {
        return timeControlSetting.getTimeControlType();
    }

    public enum TournamentFormat {
        KNOCKOUT,
        ROUND_ROBIN,
        SWISS
    }

    public enum TournamentStatus {
        UPCOMING,
        ONGOING,
        COMPLETED,
        CANCELED,
        PAUSED
    }

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getAdminId() { return adminId; }

    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Integer getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }

    public LocalDateTime getStartDateTime() { return startDateTime; }

    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }

    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public LocalDateTime getRegistrationStartDate() { return registrationStartDate; }

    public void setRegistrationStartDate(LocalDateTime registrationStartDate) { this.registrationStartDate = registrationStartDate; }

    public LocalDateTime getRegistrationEndDate() { return registrationEndDate; }

    public void setRegistrationEndDate(LocalDateTime registrationEndDate) { this.registrationEndDate = registrationEndDate; }

    public TournamentFormat getFormat() { return format; }

    public void setFormat(TournamentFormat format) { this.format = format; }

    public TimeControlSetting getTimeControlSetting() { return timeControlSetting; }

    public void setTimeControlSetting(TimeControlSetting timeControlSetting) { this.timeControlSetting = timeControlSetting; }

    public TournamentStatus getStatus() { return status; }

    public void setStatus(TournamentStatus status) { this.status = status; }

    public Integer getMinRating() { return minRating; }

    public void setMinRating(Integer minRating) { this.minRating = minRating; }

    public Integer getMaxRating() { return maxRating; }

    public void setMaxRating(Integer maxRating) { this.maxRating = maxRating; }

    public boolean isAffectsRating() { return affectsRating; }

    public void setAffectsRating(boolean affectsRating) { this.affectsRating = affectsRating; }

    public Integer getMinAge() { return minAge; }

    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    public Integer getMaxAge() { return maxAge; }

    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    public String getRequiredGender() { return requiredGender; }

    public void setRequiredGender(String requiredGender) { this.requiredGender = requiredGender; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public Set<TournamentPlayer> getParticipants() { return participants; }

    public void setParticipants(Set<TournamentPlayer> participants) { this.participants = participants; }
}
