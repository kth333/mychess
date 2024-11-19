package com.g1.mychess.tournament.util;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.model.Tournament;

/**
 * Utility class to build email content related to tournaments.
 */
public class EmailContentBuilder {

    /**
     * Builds a tournament sign-up confirmation email message.
     * 
     * @param playerDTO The player details to personalize the email.
     * @param tournament The tournament details to include in the email.
     * @return A string representing the tournament sign-up confirmation email message.
     */
    public static String buildTournamentSignUpMessage(PlayerDTO playerDTO, Tournament tournament) {
        return "Dear " + playerDTO.getUsername() + ",\n\n" +
                "You have successfully signed up for the tournament: " + tournament.getName() + ".\n\n" +
                "Good luck!\n\nMyChess Team";
    }
}
