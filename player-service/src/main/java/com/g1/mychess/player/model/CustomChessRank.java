package com.g1.mychess.player.model;

public enum CustomChessRank {
    NOVICE("Novice", 0, 1199),
    INTERMEDIATE("Intermediate", 1200, 1599),
    ADVANCED("Advanced", 1600, 1999),
    EXPERT("Expert", 2000, 2399),
    MASTER("Master", 2400, 2699),
    GRANDMASTER("Grandmaster", 2700, Double.MAX_VALUE);

    private final String title;
    private final double minRating;
    private final double maxRating;

    CustomChessRank(String title, double minRating, double maxRating) {
        this.title = title;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    public String getTitle() {
        return title;
    }

    public double getMinRating() {
        return minRating;
    }

    public double getMaxRating() {
        return maxRating;
    }

    public static CustomChessRank getRankForRating(double rating) {
        for (CustomChessRank rank : values()) {
            if (rating >= rank.minRating && rating <= rank.maxRating) {
                return rank;
            }
        }
        return NOVICE;
    }
}
