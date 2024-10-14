package com.g1.mychess.match;

import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.repository.MatchPlayerRepository;
import com.g1.mychess.match.repository.MatchRepository;
import com.g1.mychess.match.service.impl.MatchServiceImpl;
import com.g1.mychess.match.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

public class MatchServiceGlicko2Test {
    public static void main(String[] args) {
        MatchPlayer player = new MatchPlayer();
        player.setInitialRating(1500);
        player.setInitialRatingDeviation(200);
        player.setInitialVolatility(0.06);

        MatchPlayer opponent1 = new MatchPlayer();
        opponent1.setInitialRating(1400);
        opponent1.setInitialRatingDeviation(30);

        MatchPlayer opponent2 = new MatchPlayer();
        opponent2.setInitialRating(1550);
        opponent2.setInitialRatingDeviation(100);

        MatchPlayer opponent3 = new MatchPlayer();
        opponent3.setInitialRating(1700);
        opponent3.setInitialRatingDeviation(300);

        List<MatchPlayer> opponents = new ArrayList<>();
        opponents.add(opponent1);
        opponents.add(opponent2);
        opponents.add(opponent3);

        double[] result = {1.0, 0.0, 0.0};
        MatchServiceImpl.calculatePlayerRatings(player, opponents, result);

        System.out.printf("New Rating = %f\n New RD = %f\n New Volatility = %f\n", player.getNewRating(), player.getNewRatingDeviation(), player.getNewVolatility());
    }
}
