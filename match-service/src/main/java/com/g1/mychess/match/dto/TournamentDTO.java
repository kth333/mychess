package com.g1.mychess.match.dto;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a tournament.
 * This class is used to carry detailed information about a tournament, including details about the participants, tournament settings, and the tournament's status.
 */
public class TournamentDTO {

    /**
     * The unique identifier for the tournament.
     */
    private Long id;

    /**
     * The ID of the administrator (user) who created or manages the tournament.
     */
    private Long adminId;

    /**
     * The name of the tournament.
     */
    private String name;

    /**
     * A description of the tournament.
     */
    private String description;

    /**
     * The maximum number of players allowed in the tournament.
     */
    private Integer maxPlayers;

    /**
     * The scheduled start date and time of the tournament.
     */
    private LocalDateTime startDateTime;

    /**
     * The scheduled end date and time of the tournament.
     */
    private LocalDateTime endDateTime;

    /**
     * The start date and time for tournament registration.
     */
    private LocalDateTime registrationStartDate;

    /**
     * The end date and time for tournament registration.
     */
    private LocalDateTime registrationEndDate;

    /**
     * The format of the tournament (e.g., "single-elimination", "round-robin").
     */
    private String format;

    /**
     * The current status of the tournament (e.g., "active", "completed").
     */
    private String status;

    /**
     * The minimum rating required to participate in the tournament.
     */
    private Integer minRating;

    /**
     * The maximum rating allowed to participate in the tournament.
     */
    private Integer maxRating;

    /**
     * A flag indicating whether the tournament affects player ratings.
     */
    private boolean affectsRating;

    /**
     * The minimum age requirement to participate in the tournament.
     */
    private Integer minAge;

    /**
     * The maximum age requirement to participate in the tournament.
     */
    private Integer maxAge;

    /**
     * The required gender for players participating in the tournament (e.g., "male", "female").
     */
    private String requiredGender;

    /**
     * The country in which the tournament is held.
     */
    private String country;

    /**
     * The region or state in which the tournament is held.
     */
    private String region;

    /**
     * The city in which the tournament is held.
     */
    private String city;

    /**
     * The address where the tournament is held.
     */
    private String address;

    /**
     * The current round of the tournament.
     */
    private int currentRound;

    /**
     * The maximum number of rounds for the tournament.
     */
    private int maxRounds;

    /**
     * A set of players participating in the tournament.
     */
    private Set<TournamentPlayerDTO> participants;

    /**
     * The time control settings for the tournament (e.g., chess clock settings).
     */
    private TimeControlSettingDTO timeControl;

    // Constructors

    /**
     * Default constructor for TournamentDTO.
     * Initializes a new instance of the TournamentDTO class without setting any fields.
     */
    public TournamentDTO() {}

    // Getters and Setters


    /**
     * Gets the ID of the tournament.
     *
     * @return The tournament ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the tournament.
     *
     * @param id The tournament ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the admin ID of the tournament.
     *
     * @return The admin ID.
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * Sets the admin ID of the tournament.
     *
     * @param adminId The admin ID to set.
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * Gets the name of the tournament.
     *
     * @return The tournament name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tournament.
     *
     * @param name The tournament name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the maximum number of players in the tournament.
     *
     * @return The maximum number of players.
     */
    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Sets the maximum number of players in the tournament.
     *
     * @param maxPlayers The maximum number of players to set.
     */
    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /**
     * Gets the format of the tournament.
     *
     * @return The format of the tournament.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the format of the tournament.
     *
     * @param format The format to set (e.g., "single-elimination").
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Gets the current round of the tournament.
     *
     * @return The current round.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Sets the current round of the tournament.
     *
     * @param currentRound The current round to set.
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Gets the maximum number of rounds for the tournament.
     *
     * @return The maximum number of rounds.
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * Sets the maximum number of rounds for the tournament.
     *
     * @param maxRounds The maximum rounds to set.
     */
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    /**
     * Gets the list of participants in the tournament.
     *
     * @return The set of participants.
     */
    public Set<TournamentPlayerDTO> getParticipants() {
        return participants;
    }

    /**
     * Sets the list of participants in the tournament.
     *
     * @param participants The set of participants to set.
     */
    public void setParticipants(Set<TournamentPlayerDTO> participants) {
        this.participants = participants;
    }

    /**
     * Gets the time control settings for the tournament.
     *
     * @return The time control settings.
     */
    public TimeControlSettingDTO getTimeControl() {
        return timeControl;
    }

    /**
     * Sets the time control settings for the tournament.
     *
     * @param timeControl The time control settings to set.
     */
    public void setTimeControl(TimeControlSettingDTO timeControl) {
        this.timeControl = timeControl;
    }
}
