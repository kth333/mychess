package com.g1.mychess.match.service.impl;

import com.g1.mychess.match.dto.PlayerRatingUpdateDTO;
import com.g1.mychess.match.model.MatchPlayer;
import com.g1.mychess.match.service.Glicko2RatingService;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * This service class implements the Glicko-2 rating system to calculate new player ratings
 * after a match. It applies the Glicko-2 algorithm, which adjusts the player's rating based on
 * the results of their games and the ratings of their opponents.
 */
@Service
public class Glicko2RatingServiceImpl implements Glicko2RatingService {

    private static final double GLICKO_CONSTANT = 173.7178;
    private static final double GLICKO_BASE_RATING = 1500;
    private static final double PI = Math.PI;
    private static final double VOLATILITY_TAU = 0.5;
    private static final double CONVERGENCE_TOLERANCE = 0.000001;

    /**
     * Calculates the updated player ratings using the Glicko-2 system.
     *
     * @param player The player whose rating is being calculated.
     * @param opponents List of the player's opponents in the match.
     * @param result An array of results where 1.0 is a win, 0.5 is a draw, and 0.0 is a loss.
     * @return A DTO containing the updated player rating, rating deviation, and volatility.
     */
    public PlayerRatingUpdateDTO calculatePlayerRatings(MatchPlayer player, List<MatchPlayer> opponents, double[] result) {
        // Converting to Glicko-2 scale
        double R = convertToGlickoScale(player.getGlickoRating() - GLICKO_BASE_RATING);
        double RD = convertToGlickoScale(player.getRatingDeviation());

        double[] opponentsRating = new double[opponents.size()];
        double[] opponentsRD = new double[opponents.size()];

        for (int j = 0; j < opponents.size(); j++) {
            opponentsRating[j] = convertToGlickoScale(opponents.get(j).getGlickoRating() - GLICKO_BASE_RATING);
            opponentsRD[j] = convertToGlickoScale(opponents.get(j).getRatingDeviation());
        }

        // Calculate Glicko-2 rating changes
        double delta = calculate_delta(R, opponentsRating, opponentsRD, result);
        double v = calculate_v(R, opponentsRating, opponentsRD);
        double newVolatility = calculate_volatility(RD, player.getVolatility(), v, delta * delta);
        double newRatingDeviation = calculateNewRatingDeviation(RD, newVolatility, v);
        double newRating = calculateNewRating(R, newRatingDeviation, delta, v);

        // Convert back to Glicko scale and round values for final DTO
        return new PlayerRatingUpdateDTO(
                player.getPlayerId(),
                Math.round(convertFromGlickoScale(newRating + GLICKO_BASE_RATING)),              // Glicko scale rating
                Math.round(convertFromGlickoScale(newRatingDeviation) * 10) / 10.0, // Glicko scale RD, 1 decimal place
                Math.round(newVolatility * 1000) / 1000.0             // Volatility, 3 decimal places
        );
    }

    /**
     * Converts a rating from the Glicko scale to the Glicko-2 scale.
     *
     * @param value The value to be converted.
     * @return The converted rating in Glicko-2 scale.
     */
    private double convertToGlickoScale(double value) {
        return value / GLICKO_CONSTANT;
    }

    // Convert from Glicko-2 scale to standard Glicko scale
    private double convertFromGlickoScale(double value) {
        return GLICKO_CONSTANT * value;
    }

    /**
     * Calculates the "g" function used in the Glicko-2 system, which scales the rating deviation.
     *
     * @param RD The rating deviation of the player.
     * @return The value of "g(RD)".
     */
    public static double calculate_g(double RD) {
        return 1.0 / Math.sqrt(1.0 + (3.0 * RD * RD) / (Math.PI * Math.PI));
    }

    /**
     * Calculates the expected probability of the player winning against an opponent.
     *
     * @param R The rating of the player.
     * @param Rj The rating of the opponent.
     * @param RDj The rating deviation of the opponent.
     * @return The expected probability of the player winning.
     */
    public static double calculate_E(double R, double Rj, double RDj) {
        return 1.0 / (1.0 + Math.exp(-calculate_g(RDj) * (R - Rj)));
    }

    /**
     * Calculates the "v" function, which represents the total variance for a set of opponents.
     *
     * @param R The rating of the player.
     * @param opponents_rating The ratings of the opponents.
     * @param opponents_RD The rating deviations of the opponents.
     * @return The calculated value of "v".
     */
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

    /**
     * Calculates the delta, which is the difference between the current and expected ratings,
     * weighted by the variance.
     *
     * @param R The rating of the player.
     * @param opponents_rating The ratings of the opponents.
     * @param opponents_RD The rating deviations of the opponents.
     * @param results The results of the match (1.0 for win, 0.5 for draw, 0.0 for loss).
     * @return The calculated delta.
     */
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

    /**
     * A helper function used in the calculation of volatility. It models the relationship
     * between the volatility, rating deviation, and variance.
     *
     * @param x The current volatility guess.
     * @param delta_squared The squared delta value.
     * @param RD_squared The squared rating deviation.
     * @param v The total variance.
     * @param A The initial guess for the volatility.
     * @return The result of the function.
     */
    public static double calculate_function(double x, double delta_squared, double RD_squared, double v, double A) {
        double expX = Math.exp(x);

        double numerator = expX * Math.pow(delta_squared - RD_squared - v - expX, 2);
        double denominator = Math.pow(RD_squared + v + expX, 2);

        double firstPart = numerator / denominator;
        double secondPart = (x - A) / (VOLATILITY_TAU * VOLATILITY_TAU);

        return firstPart - secondPart;
    }

    /**
     * Calculates the volatility of a player's rating, which measures the degree of uncertainty in the player's performance.
     *
     * @param RD The rating deviation of the player.
     * @param volatility The current volatility of the player.
     * @param v The total variance.
     * @param delta_squared The squared delta value.
     * @return The updated volatility.
     */
    public static double calculate_volatility(double RD, double volatility, double v, double delta_squared) {
        double A = Math.log(Math.pow(volatility, 2));
        double B;
        double convergence_tolerance = 0.000001;

        if (delta_squared > (RD * RD) + v) {
            B = Math.log(delta_squared - (RD * RD) - v);
        } else {
            int k = 1;
            while (calculate_function(A - k * VOLATILITY_TAU, delta_squared, RD * RD, v, A) < 0) { // tau is set at 0.5
                k++;
            }
            B = A - k * VOLATILITY_TAU;
        }

        double f_A = calculate_function(A, delta_squared, RD * RD, v, A);
        double f_B = calculate_function(B, delta_squared, RD * RD, v, A);

        while (Math.abs(B - A) > CONVERGENCE_TOLERANCE) {
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