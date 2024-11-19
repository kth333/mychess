package com.g1.mychess.tournament.model;

import jakarta.persistence.Embeddable;

/**
 * Represents the time control settings for a chess game, including the base time (in minutes)
 * and the increment time (in seconds) for each move.
 * This class is embedded into the {@link Tournament} entity.
 */
@Embeddable
public class TimeControlSetting {

    private int baseTimeMinutes;
    private int incrementSeconds;

    /**
     * Constructs a new {@link TimeControlSetting} with the specified base time and increment time.
     *
     * @param baseTimeMinutes the base time in minutes
     * @param incrementSeconds the increment time in seconds
     */
    public TimeControlSetting(int baseTimeMinutes, int incrementSeconds) {
        this.baseTimeMinutes = baseTimeMinutes;
        this.incrementSeconds = incrementSeconds;
    }

    /**
     * Default constructor for JPA.
     */
    public TimeControlSetting() {
    }

    /**
     * Returns the base time in minutes.
     *
     * @return the base time in minutes
     */
    public int getBaseTimeMinutes() {
        return baseTimeMinutes;
    }

    /**
     * Sets the base time in minutes.
     *
     * @param baseTimeMinutes the base time in minutes
     */
    public void setBaseTimeMinutes(int baseTimeMinutes) {
        this.baseTimeMinutes = baseTimeMinutes;
    }

    /**
     * Returns the increment time in seconds.
     *
     * @return the increment time in seconds
     */
    public int getIncrementSeconds() {
        return incrementSeconds;
    }

    /**
     * Sets the increment time in seconds.
     *
     * @param incrementSeconds the increment time in seconds
     */
    public void setIncrementSeconds(int incrementSeconds) {
        this.incrementSeconds = incrementSeconds;
    }

    /**
     * Determines the type of time control based on the base time.
     * 
     * @return the {@link TimeControlType} corresponding to the base time
     */
    public TimeControlType getTimeControlType() {
        if (baseTimeMinutes < 3) {
            return TimeControlType.BULLET;
        } else if (baseTimeMinutes < 10) {
            return TimeControlType.BLITZ;
        } else if (baseTimeMinutes < 60) {
            return TimeControlType.RAPID;
        } else {
            return TimeControlType.CLASSICAL;
        }
    }

    /**
     * Returns a string representation of the time control setting in the format "baseTime+increment".
     *
     * @return the string representation of the time control setting
     */
    @Override
    public String toString() {
        return baseTimeMinutes + "+" + incrementSeconds;
    }

    /**
     * Enum representing the types of time control used in chess games.
     */
    public enum TimeControlType {
        BULLET,    // Time control for very fast games (typically under 3 minutes per player)
        BLITZ,     // Time control for fast games (typically under 10 minutes per player)
        RAPID,     // Time control for moderate-paced games (typically under 60 minutes per player)
        CLASSICAL  // Time control for long-form games (typically 1 hour or more per player)
    }
}
