package com.g1.mychess.tournament.dto;

import com.g1.mychess.tournament.model.TimeControlSetting;

import java.time.LocalDateTime;
import java.util.Set;

public class TournamentDTO {

    private Long id;
    private Long adminId;
    private String name;
    private String description;
    private Integer maxPlayers;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime registrationStartDate;
    private LocalDateTime registrationEndDate;
    private String format;
    private String status;
    private Integer minRating;
    private Integer maxRating;
    private boolean affectsRating;
    private Integer minAge;
    private Integer maxAge;
    private String requiredGender;
    private String country;
    private String region;
    private String city;
    private String address;
    private int currentRound;
    private int maxRounds;
    private Set<TournamentPlayerDTO> participants;
    private TimeControlSetting timeControl;

    // Constructors
    public TournamentDTO() {}

    public TournamentDTO(Long id, Long adminId, String name, String description, Integer maxPlayers,
                         LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime registrationStartDate,
                         LocalDateTime registrationEndDate, String format, String status, Integer minRating,
                         Integer maxRating, boolean affectsRating, Integer minAge, Integer maxAge, String requiredGender,
                         String country, String region, String city, String address, int currentRound, int maxRounds,
                         Set<TournamentPlayerDTO> participants, TimeControlSetting timeControlSetting) {
        this.id = id;
        this.adminId = adminId;
        this.name = name;
        this.description = description;
        this.maxPlayers = maxPlayers;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.registrationStartDate = registrationStartDate;
        this.registrationEndDate = registrationEndDate;
        this.format = format;
        this.status = status;
        this.minRating = minRating;
        this.maxRating = maxRating;
        this.affectsRating = affectsRating;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.requiredGender = requiredGender;
        this.country = country;
        this.region = region;
        this.city = city;
        this.address = address;
        this.currentRound = currentRound;
        this.maxRounds = maxRounds;
        this.participants = participants;
        this.timeControl = timeControlSetting;
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

    public String getFormat() { return format; }

    public void setFormat(String format) { this.format = format; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

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

    public Set<TournamentPlayerDTO> getParticipants() { return participants; }

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

    public void setParticipants(Set<TournamentPlayerDTO> participants) { this.participants = participants; }

    public TimeControlSetting getTimeControlSetting() { return timeControl; }

    public void setTimeControlSetting(TimeControlSetting timeControl) { this.timeControl = timeControl; }

    public void setTimeControl(TimeControlSetting timeControl) { this.timeControl = timeControl; }
}
