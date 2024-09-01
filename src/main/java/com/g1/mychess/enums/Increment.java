package com.g1.mychess.enums;

public enum Increment {
    NONE(0),
    ONE_SECOND(1),
    TWO_SECONDS(2),
    THREE_SECONDS(3),
    FIVE_SECONDS(5),
    TEN_SECONDS(10),
    FIFTEEN_SECONDS(15);

    private final int seconds;

    Increment(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }
}
