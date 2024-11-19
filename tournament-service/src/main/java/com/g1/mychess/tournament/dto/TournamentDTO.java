package com.g1.mychess.tournament.dto;

import com.g1.mychess.tournament.model.TimeControlSetting;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Data Transfer Object representing a tournament.
 */
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

    /**
     * Default constructor.
     */
    public TournamentDTO() {}

    /**
     * Constructs a new {@code TournamentDTO} with the specified details.
     *
     * @param id                    the ID of the tournament
     * @param adminId               the ID of the admin who created the tournament
     * @param name                  the name of the tournament
     * @param description           the description of the tournament
     * @param maxPlayers            the maximum number of players allowed in the tournament
     * @param startDateTime         the start date and time of the tournament
     * @param endDateTime           the end date and time of the tournament
     * @param registrationStartDate the registration start date and time
     * @param registrationEndDate   the registration end date and time
     * @param format                the format of the tournament
     * @param status                the current status of the tournament
     * @param minRating             the minimum rating required to participate
     * @param maxRating             the maximum rating allowed to participate
     * @param affectsRating         whether the tournament affects player ratings
     * @param minAge                the minimum age required to participate
     * @param maxAge                the maximum age allowed to participate
     * @param requiredGender        the required gender to participate
     * @param country               the country in which the tournament is held
     * @param region                the region in which the tournament is held
     * @param city                  the city in which the tournament is held
     * @param address               the address of the tournament location
     * @param currentRound          the current round of the tournament
     * @param maxRounds             the maximum number of rounds in the tournament
     * @param participants          the set of participants in the tournament
     * @param timeControlSetting    the time control setting for the tournament
     */
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

    /**
     * Returns the tournament's ID.
     *
     * @return the tournament's ID
     */
    public Long getId() { return id; }

    /**
     * Sets the tournament's ID.
     *
     * @param id the tournament's ID
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Returns the admin ID.
     *
     * @return the admin's ID
     */
    public Long getAdminId() { return adminId; }

    /**
     * Sets the admin's ID.
     *
     * @param adminId the admin's ID
     */
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    /**
     * Returns the name of the tournament.
     *
     * @return the tournament's name
     */
    public String getName() { return name; }

    /**
     * Sets the name of the tournament.
     *
     * @param name the tournament's name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Returns the description of the tournament.
     *
     * @return the tournament's description
     */
    public String getDescription() { return description; }

    /**
     * Sets the description of the tournament.
     *
     * @param description the tournament's description
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Returns the maximum number of players allowed in the tournament.
     *
     * @return the maximum number of players
     */
    public Integer getMaxPlayers() { return maxPlayers; }

    /**
     * Sets the maximum number of players allowed in the tournament.
     *
     * @param maxPlayers the maximum number of players
     */
    public void setMaxPlayers(Integer maxPlayers) { this.maxPlayers = maxPlayers; }

    /**
     * Returns the start date and time of the tournament.
     *
     * @return the start date and time
     */
    public LocalDateTime getStartDateTime() { return startDateTime; }

    /**
     * Sets the start date and time of the tournament.
     *
     * @param startDateTime the start date and time
     */
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    /**
     * Returns the end date and time of the tournament.
     *
     * @return the end date and time
     */
    public LocalDateTime getEndDateTime() { return endDateTime; }

    /**
     * Sets the end date and time of the tournament.
     *
     * @param endDateTime the end date and time
     */
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    /**
     * Returns the registration start date and time.
     *
     * @return the registration start date and time
     */
    public LocalDateTime getRegistrationStartDate() { return registrationStartDate; }

    /**
     * Sets the registration start date and time.
     *
     * @param registrationStartDate the registration start date and time
     */
    public void setRegistrationStartDate(LocalDateTime registrationStartDate) { this.registrationStartDate = registrationStartDate; }

    /**
     * Returns the registration end date and time.
     *
     * @return the registration end date and time
     */
    public LocalDateTime getRegistrationEndDate() { return registrationEndDate; }

    /**
     * Sets the registration end date and time.
     *
     * @param registrationEndDate the registration end date and time
     */
    public void setRegistrationEndDate(LocalDateTime registrationEndDate) { this.registrationEndDate = registrationEndDate; }

    /**
     * Returns the format of the tournament.
     *
     * @return the tournament's format
     */
    public String getFormat() { return format; }

    /**
     * Sets the format of the tournament.
     *
     * @param format the tournament's format
     */
    public void setFormat(String format) { this.format = format; }

    /**
     * Returns the current status of the tournament.
     *
     * @return the tournament's status
     */
    public String getStatus() { return status; }

    /**
     * Sets the status of the tournament.
     *
     * @param status the tournament's status
     */
    public void setStatus(String status) { this.status = status; }

    /**
     * Returns the minimum rating required to participate in the tournament.
     *
     * @return the minimum rating
     */
    public Integer getMinRating() { return minRating; }

    /**
     * Sets the minimum rating required to participate.
     *
     * @param minRating the minimum rating
     */
    public void setMinRating(Integer minRating) { this.minRating = minRating; }

    /**
     * Returns the maximum rating allowed to participate in the tournament.
     *
     * @return the maximum rating
     */
    public Integer getMaxRating() { return maxRating; }

    /**
     * Sets the maximum rating allowed to participate.
     *
     * @param maxRating the maximum rating
     */
    public void setMaxRating(Integer maxRating) { this.maxRating = maxRating; }

    /**
     * Returns whether the tournament affects player ratings.
     *
     * @return {@code true} if the tournament affects player ratings, otherwise {@code false}
     */
    public boolean isAffectsRating() { return affectsRating; }

    /**
     * Sets whether the tournament affects player ratings.
     *
     * @param affectsRating {@code true} if the tournament affects player ratings, otherwise {@code false}
     */
    public void setAffectsRating(boolean affectsRating) { this.affectsRating = affectsRating; }

    /**
     * Returns the minimum age required to participate in the tournament.
     *
     * @return the minimum age
     */
    public Integer getMinAge() { return minAge; }

    /**
     * Sets the minimum age required to participate.
     *
     * @param minAge the minimum age
     */
    public void setMinAge(Integer minAge) { this.minAge = minAge; }

    /**
     * Returns the maximum age allowed to participate in the tournament.
     *
     * @return the maximum age
     */
    public Integer getMaxAge() { return maxAge; }

    /**
     * Sets the maximum age allowed to participate.
     *
     * @param maxAge the maximum age
     */
    public void setMaxAge(Integer maxAge) { this.maxAge = maxAge; }

    /**
     * Returns the required gender to participate in the tournament.
     *
     * @return the required gender
     */
    public String getRequiredGender() { return requiredGender; }

    /**
     * Sets the required gender to participate.
     *
     * @param requiredGender the required gender
     */
    public void setRequiredGender(String requiredGender) { this.requiredGender = requiredGender; }

    /**
     * Returns the country where the tournament is held.
     *
     * @return the country
     */
    public String getCountry() { return country; }

    /**
     * Sets the country where the tournament is held.
     *
     * @param country the country
     */
    public void setCountry(String country) { this.country = country; }

    /**
     * Returns the region where the tournament is held.
     *
     * @return the region
     */
    public String getRegion() { return region; }

    /**
     * Sets the region where the tournament is held.
     *
     * @param region the region
     */
    public void setRegion(String region) { this.region = region; }

    /**
     * Returns the city where the tournament is held.
     *
     * @return the city
     */
    public String getCity() { return city; }

    /**
     * Sets the city where the tournament is held.
     *
     * @param city the city
     */
    public void setCity(String city) { this.city = city; }

    /**
     * Returns the address where the tournament is held.
     *
     * @return the address
     */
    public String getAddress() { return address; }

    /**
     * Sets the address where the tournament is held.
     *
     * @param address the address
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Returns the participants of the tournament.
     *
     * @return the set of participants
     */
    public Set<TournamentPlayerDTO> getParticipants() { return participants; }

    /**
     * Sets the participants of the tournament.
     *
     * @param participants the set of participants
     */
    public void setParticipants(Set<TournamentPlayerDTO> participants) { this.participants = participants; }

    /**
     * Returns the current round of the tournament.
     *
     * @return the current round
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Sets the current round of the tournament.
     *
     * @param currentRound the current round
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Returns the maximum number of rounds in the tournament.
     *
     * @return the maximum rounds
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * Sets the maximum number of rounds in the tournament.
     *
     * @param maxRounds the maximum rounds
     */
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    /**
     * Returns the time control setting for the tournament.
     *
     * @return the time control setting
     */
    public TimeControlSetting getTimeControlSetting() { return timeControl; }

    /**
     * Sets the time control setting for the tournament.
     *
     * @param timeControl the time control setting
     */
    public void setTimeControlSetting(TimeControlSetting timeControl) { this.timeControl = timeControl; }
}
