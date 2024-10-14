package com.g1.mychess.match.service;

import com.g1.mychess.match.model.MatchPlayer;
import java.util.List;

public interface Glicko2RatingService {
    void calculatePlayerRatings(MatchPlayer player, List<MatchPlayer> opponents, double[] results);
}