package com.g1.mychess.tournament.validation;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.exception.PlayerBlacklistedException;
import com.g1.mychess.tournament.exception.PlayerIneligibleException;
import com.g1.mychess.tournament.model.Tournament;

/**
 * Utility class to check if a player is eligible to participate in a tournament.
 */
public class PlayerEligibilityChecker {

    /**
     * Checks the eligibility of a player for a given tournament based on various criteria.
     * 
     * @param playerDTO The player details to be checked.
     * @param tournament The tournament the player is attempting to participate in.
     * @throws PlayerBlacklistedException if the player is blacklisted from participating in tournaments.
     * @throws PlayerIneligibleException if the player does not meet the tournament's rating, age, or gender requirements.
     */
    public static void checkPlayerEligibility(PlayerDTO playerDTO, Tournament tournament) {
        if (playerDTO.isBlacklisted()) {
            throw new PlayerBlacklistedException("Player is blacklisted from participating in tournaments.");
        }
        if (playerDTO.getGlickoRating() < tournament.getMinRating() || playerDTO.getGlickoRating() > tournament.getMaxRating()) {
            throw new PlayerIneligibleException("Player does not meet the rating requirements.");
        }
        if (tournament.getMinAge() != null && playerDTO.getAge() < tournament.getMinAge()) {
            throw new PlayerIneligibleException("Player does not meet the minimum age requirement.");
        }
        if (tournament.getMaxAge() != null && playerDTO.getAge() > tournament.getMaxAge()) {
            throw new PlayerIneligibleException("Player does not meet the maximum age requirement.");
        }
        if (tournament.getRequiredGender() != null && !tournament.getRequiredGender().equalsIgnoreCase("ANY")
                && !tournament.getRequiredGender().equalsIgnoreCase(playerDTO.getGender())) {
            throw new PlayerIneligibleException("Player does not meet the gender requirement.");
        }
    }
}
