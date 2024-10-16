package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.service.Glicko2RatingService;
import com.g1.mychess.match.exception.InvalidRatingException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class Glicko2RatingServiceImpl implements Glicko2RatingService {

    private static final double SCALE_FACTOR = 173.7178;

    public PlayerRatingUpdateDTO calculatePlayerRatings(MatchPlayer player, List<MatchPlayer> opponents, double[] result) {
        // converting rating and rating deviation to glicko-2 scale
        try {
            System.out.println("Calculating ratings for player: " + player.getPlayerId());
            System.out.print("Initial rating: " + player.getGlickoRating() + " Initial RD: " + player.getRatingDeviation() + " Initial Volatility: " + player.getVolatility());

            double R = (player.getGlickoRating() - 1500) / SCALE_FACTOR;
            double RD = player.getRatingDeviation() / SCALE_FACTOR;

            double[] opponents_rating = new double[opponents.size()];
            double[] opponents_RD = new double[opponents.size()];

            for (int j = 0; j < opponents.size(); j++) {
                opponents_rating[j] = (opponents.get(j).getGlickoRating() - 1500) / SCALE_FACTOR;
                opponents_RD[j] = opponents.get(j).getRatingDeviation() / SCALE_FACTOR;
            }

            double delta = calculate_delta(R, opponents_rating, opponents_RD, result);
            double v = calculate_v(R, opponents_rating, opponents_RD);

            double newVolatility = calculate_volatility(RD, player.getVolatility(), v, delta * delta);
            double newRatingDeviation = calculateNewRatingDeviation(RD, newVolatility, v);
            double newRating = calculateNewRating(R, newRatingDeviation, delta, v);

            System.out.println("New rating calculated for playerId: " + player.getPlayerId() +
                    " New Rating: " + (SCALE_FACTOR * newRating + SCALE_FACTOR) +
                    " New Rating Deviation: " + (SCALE_FACTOR * newRatingDeviation) +
                    " New Volatility: " + newVolatility);

            return new PlayerRatingUpdateDTO(
                    player.getPlayerId(),
                    SCALE_FACTOR * newRating + 1500,            // Convert back to original scale
                    SCALE_FACTOR * newRatingDeviation,          // Convert back to original scale
                    newVolatility
            );
        } catch (InvalidRatingException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("An error occured while calculating the player ratings.", e);
        }
    }

    public static double calculate_g(double RD) {
        return 1.0 / Math.sqrt(1.0 + (3.0 * RD * RD) / (Math.PI * Math.PI));
    }

    public static double calculate_E(double R, double Rj, double RDj) {
        return 1.0 / (1.0 + Math.exp(-calculate_g(RDj) * (R - Rj)));
    }

    public static double calculate_v(double R, double[] opponents_rating, double[] opponents_RD) {
        double v_inverse = 0;
        for (int j = 0; j < opponents_rating.length; j++) {
            double Rj = opponents_rating[j];
            double RDj = opponents_RD[j];
            double g_RDj = calculate_g(RDj);
            double E_R_Rj = calculate_E(R, Rj, RDj);
            v_inverse += g_RDj * g_RDj * E_R_Rj * (1 - E_R_Rj);
        }
        return 1 / v_inverse;
    }

    public static double calculate_delta(double R, double[] opponents_rating, double[] opponents_RD, double[] results) {
        double temp = 0;
        for (int j = 0; j < opponents_rating.length; j++) {
            double Rj = opponents_rating[j];
            double RDj = opponents_RD[j];
            double g_RDj = calculate_g(RDj);
            double E_R_Rj = calculate_E(R, Rj, RDj);
            temp += g_RDj * (results[j] - E_R_Rj);
        }
        return calculate_v(R, opponents_rating, opponents_RD) * temp;
    }

    public static double calculate_function(double x, double delta_squared, double RD_squared, double v, double A) {
        double expX = Math.exp(x);

        double numerator = expX * Math.pow(delta_squared - RD_squared - v - expX, 2);
        double denominator = Math.pow(RD_squared + v + expX, 2);

        double firstPart = numerator / denominator;
        double secondPart = (x - A) / (0.5 * 0.5);

        return firstPart - secondPart;
    }

    public static double calculate_volatility(double RD, double volatility, double v, double delta_squared) {
        double A = Math.log(Math.pow(volatility, 2));
        double B;
        double convergence_tolerance = 0.000001;

        if (delta_squared > (RD * RD) + v) {
            B = Math.log(delta_squared - (RD * RD) - v);
        } else {
            int k = 1;
            while (calculate_function(A - k * 0.5, delta_squared, RD * RD, v, A) < 0) { // tau is set at 0.5
                k++;
            }
            B = A - k * 0.5;
        }

        double f_A = calculate_function(A, delta_squared, RD * RD, v, A);
        double f_B = calculate_function(B, delta_squared, RD * RD, v, A);

        while (Math.abs(B - A) > convergence_tolerance) {
            double C = A + ((A - B) * f_A) / (f_B - f_A);
            double f_C = calculate_function(C, delta_squared, RD * RD, v, A);

            if (f_C * f_B <= 0) {
                A = B;
                f_A = f_B;
            } else {
                f_A /= 2;
            }
            B = C;
            f_B = f_C;
        }

        return Math.exp(A / 2);
    }

    private double calculateNewRatingDeviation(double RD, double newVolatility, double v) {
        double pre_rating_RD = Math.sqrt(RD * RD + newVolatility * newVolatility);
        return 1 / Math.sqrt((1 / pre_rating_RD / pre_rating_RD) + (1 / v));
    }

    private double calculateNewRating(double R, double newRD, double delta, double v) {
        return R + newRD * newRD * delta / v;
    }
}