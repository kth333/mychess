package com.g1.mychess.tournament.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class TimeControlSetting {

    private int baseTimeMinutes;
    private int incrementSeconds;

    public TimeControlSetting(int baseTimeMinutes, int incrementSeconds) {
        this.baseTimeMinutes = baseTimeMinutes;
        this.incrementSeconds = incrementSeconds;
    }

    public TimeControlSetting() {
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

    @Override
    public String toString() {
        return baseTimeMinutes + "+" + incrementSeconds;
    }

    public enum TimeControlType {
        BULLET,
        BLITZ,
        RAPID,
        CLASSICAL
    }
}
