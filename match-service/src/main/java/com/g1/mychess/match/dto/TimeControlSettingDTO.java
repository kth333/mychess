package com.g1.mychess.match.dto;

/**
 * Data Transfer Object (DTO) representing the time control settings for a chess game or tournament.
 * This class holds information about the base time for each player and the increment per move.
 */
public class TimeControlSettingDTO {

    /**
     * The base time (in minutes) allotted to each player at the start of the game or tournament.
     */
    private int baseTimeMinutes;

    /**
     * The number of seconds added to a player's clock after each move.
     */
    private int incrementSeconds;

    // Constructors

    /**
     * Default constructor for TimeControlSettingDTO.
     * Initializes a new instance of the TimeControlSettingDTO class without setting any fields.
     */
    public TimeControlSettingDTO() {
    }

    /**
     * Constructor for TimeControlSettingDTO with specified base time and increment.
     *
     * @param baseTimeMinutes  The base time (in minutes) allotted to each player.
     * @param incrementSeconds The number of seconds added to a player's clock after each move.
     */
    public TimeControlSettingDTO(int baseTimeMinutes, int incrementSeconds) {
        this.baseTimeMinutes = baseTimeMinutes;
        this.incrementSeconds = incrementSeconds;
    }

    // Getters and Setters

    /**
     * Gets the base time (in minutes) allotted to each player.
     *
     * @return The base time in minutes.
     */
    public int getBaseTimeMinutes() {
        return baseTimeMinutes;
    }

    /**
     * Sets the base time (in minutes) allotted to each player.
     *
     * @param baseTimeMinutes The base time to set, in minutes.
     */
    public void setBaseTimeMinutes(int baseTimeMinutes) {
        this.baseTimeMinutes = baseTimeMinutes;
    }

    /**
     * Gets the increment (in seconds) added to a player's clock after each move.
     *
     * @return The increment in seconds.
     */
    public int getIncrementSeconds() {
        return incrementSeconds;
    }

    /**
     * Sets the increment (in seconds) added to a player's clock after each move.
     *
     * @param incrementSeconds The increment to set, in seconds.
     */
    public void setIncrementSeconds(int incrementSeconds) {
        this.incrementSeconds = incrementSeconds;
    }
}