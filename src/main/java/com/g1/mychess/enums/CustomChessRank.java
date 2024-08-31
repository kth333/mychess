package com.g1.mychess.enums;

public enum CustomChessRank {
    NOVICE("Novice", 0, 1199),
    INTERMEDIATE("Intermediate", 1200, 1599),
    ADVANCED("Advanced", 1600, 1999),
    EXPERT("Expert", 2000, 2399),
    MASTER("Master", 2400, Integer.MAX_VALUE);

    private final String title;
    private final int minRating;
    private final int maxRating;

    CustomChessRank(String title, int minRating, int maxRating) {
        this.title = title;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public String getTitle() {
        return title;
    }

    public int getMinRating() {
        return minRating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public static CustomChessRank getRankForRating(int rating) {
        for (CustomChessRank rank : values()) {
            if (rating >= rank.minRating && rating <= rank.maxRating) {
                return rank;
            }
        }
        return NOVICE;
    }
}
