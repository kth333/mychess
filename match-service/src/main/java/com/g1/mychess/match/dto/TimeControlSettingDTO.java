package com.g1.mychess.match.dto;

public class TimeControlSettingDTO {

    private int baseTimeMinutes;
    private int incrementSeconds;

    // Constructors, getters, setters

    public TimeControlSettingDTO() {}

    public TimeControlSettingDTO(int baseTimeMinutes, int incrementSeconds) {
        this.baseTimeMinutes = baseTimeMinutes;
        this.incrementSeconds = incrementSeconds;
    }

    public int getBaseTimeMinutes() {
        return baseTimeMinutes;
    }

    public void setBaseTimeMinutes(int baseTimeMinutes) {
        this.baseTimeMinutes = baseTimeMinutes;
    }

    public int getIncrementSeconds() {
        return incrementSeconds;
    }

    public void setIncrementSeconds(int incrementSeconds) {
        this.incrementSeconds = incrementSeconds;
    }
}