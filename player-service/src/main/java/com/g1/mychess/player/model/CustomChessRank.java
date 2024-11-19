package com.g1.mychess.player.model;

/**
 * Enum representing custom chess ranks based on rating ranges.
 * Each rank has a title and a rating range, and the enum provides methods to retrieve the rank based on a rating.
 */
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

    /**
     * Constructor to initialize a custom chess rank with a title and rating range.
     *
     * @param title the title of the rank (e.g., Novice, Expert).
     * @param minRating the minimum rating required for this rank.
     * @param maxRating the maximum rating allowed for this rank.
     */
    CustomChessRank(String title, double minRating, double maxRating) {
        this.title = title;
        this.minRating = minRating;
        this.maxRating = maxRating;
    }

    /**
     * Gets the title of the rank.
     *
     * @return the title of the rank (e.g., "Novice", "Master").
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the minimum rating required for this rank.
     *
     * @return the minimum rating for the rank.
     */
    public double getMinRating() {
        return minRating;
    }

    /**
     * Gets the maximum rating allowed for this rank.
     *
     * @return the maximum rating for the rank.
     */
    public double getMaxRating() {
        return maxRating;
    }

    /**
     * Returns the chess rank corresponding to a given rating.
     * If the rating does not fall within the defined range, it defaults to NOVICE.
     *
     * @param rating the rating to find the corresponding rank for.
     * @return the corresponding CustomChessRank based on the rating.
     */
    public static CustomChessRank getRankForRating(double rating) {
        for (CustomChessRank rank : values()) {
            if (rating >= rank.minRating && rating <= rank.maxRating) {
                return rank;
            }
        }
        return NOVICE;
    }
}