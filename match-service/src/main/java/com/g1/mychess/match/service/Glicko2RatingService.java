package com.g1.mychess.match.service;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.model.MatchPlayer;
import java.util.List;

/**
 * Interface for Glicko2RatingService.
 * <p>
 * This interface defines the contract for a service that calculates player ratings
 * based on the Glicko-2 rating system. The Glicko-2 system adjusts a player's rating
 * based on the results of matches played against multiple opponents, factoring in
 * rating deviation (RD) and volatility.
 * </p>
 */
public interface Glicko2RatingService {

    /**
     * Calculates the updated player ratings based on the Glicko-2 system.
     * <p>
     * The method calculates the updated rating, rating deviation (RD), and volatility
     * for the given player after considering their performance against multiple opponents.
     * The results array contains the outcome of the player's matches against each opponent.
     * </p>
     *
     * @param player      The {@link MatchPlayer} representing the player whose rating is to be calculated.
     * @param opponents   A list of {@link MatchPlayer} objects representing the opponents the player competed against.
     * @param results     An array of {@code double} representing the results of the matches. Each result corresponds
     *                    to the outcome of the match between the player and an opponent (1 for win, 0 for loss,
     *                    and 0.5 for draw).
     * @return            A {@link PlayerRatingUpdateDTO} object containing the updated player rating, rating deviation (RD),
     *                    and volatility.
     */
    PlayerRatingUpdateDTO calculatePlayerRatings(MatchPlayer player, List<MatchPlayer> opponents, double[] results);
}