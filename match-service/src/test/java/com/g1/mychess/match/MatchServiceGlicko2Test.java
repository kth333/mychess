package com.g1.mychess.match;

import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.service.impl.*;
import com.g1.mychess.match.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

public class MatchServiceGlicko2Test {
    public static void main(String[] args) {
        MatchPlayer player = new MatchPlayer();
        player.setGlickoRating(1500);
        player.setRatingDeviation(200);
        player.setVolatility(0.06);

        MatchPlayer opponent1 = new MatchPlayer();
        opponent1.setGlickoRating(1400);
        opponent1.setRatingDeviation(30);

        MatchPlayer opponent2 = new MatchPlayer();
        opponent2.setGlickoRating(1550);
        opponent2.setRatingDeviation(100);

        MatchPlayer opponent3 = new MatchPlayer();
        opponent3.setGlickoRating(1700);
        opponent3.setRatingDeviation(300);

        List<MatchPlayer> opponents = new ArrayList<>();
        opponents.add(opponent1);
        opponents.add(opponent2);
        opponents.add(opponent3);

        double[] result = {1.0, 0.0, 0.0};
        Glicko2RatingServiceImpl glicko2RatingService = new Glicko2RatingServiceImpl();
        glicko2RatingService.calculatePlayerRatings(player, opponents, result);

//      System.out.printf("New Rating = %f\n New RD = %f\n New Volatility = %f\n", player.getNewRating(), player.getNewRatingDeviation(), player.getNewVolatility());
    }
}
