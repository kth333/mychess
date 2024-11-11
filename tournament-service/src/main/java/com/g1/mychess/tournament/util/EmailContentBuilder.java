package com.g1.mychess.tournament.util;

import com.g1.mychess.tournament.dto.PlayerDTO;
import com.g1.mychess.tournament.model.Tournament;

public class EmailContentBuilder {

    public static String buildTournamentSignUpMessage(PlayerDTO playerDTO, Tournament tournament) {
        return "Dear " + playerDTO.getUsername() + ",\n\n" +
                "You have successfully signed up for the tournament: " + tournament.getName() + ".\n\n" +
                "Good luck!\n\nMyChess Team";
    }
}
