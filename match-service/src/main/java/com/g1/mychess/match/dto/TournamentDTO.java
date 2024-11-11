package com.g1.mychess.match.dto;

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
    private TimeControlSettingDTO timeControl;

    // Constructors
    public TournamentDTO() {}

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }


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

    public Set<TournamentPlayerDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<TournamentPlayerDTO> participants) {
        this.participants = participants;
    }

    public TimeControlSettingDTO getTimeControl() {
        return timeControl;
    }

    public void setTimeControl(TimeControlSettingDTO timeControl) {
        this.timeControl = timeControl;
    }
}
