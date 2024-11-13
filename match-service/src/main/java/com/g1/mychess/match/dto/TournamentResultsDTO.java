package com.g1.mychess.match.dto;

import java.util.List;

/**
 * Data Transfer Object (DTO) to encapsulate the results of a tournament.
 * This object contains the tournament's ID and a list of player results.
 */
public class TournamentResultsDTO {

    /**
     * The unique identifier of the tournament.
     */
    private Long tournamentId;

    /**
     * A list of player results in the tournament.
     * Each result includes the player's ID and the points they earned.
     */
    private List<PlayerResultDTO> playerResults;

    /**
     * Constructor to initialize the tournament results with the tournament ID and player results.
     *
     * @param tournamentId the ID of the tournament.
     * @param playerResults the list of player results.
     */
    public TournamentResultsDTO(Long tournamentId, List<PlayerResultDTO> playerResults) {
        this.tournamentId = tournamentId;
        this.playerResults = playerResults;
    }

    /**
     * Gets the ID of the tournament.
     *
     * @return the tournament ID.
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the ID of the tournament.
     *
     * @param tournamentId the ID to set for the tournament.
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Gets the list of player results for the tournament.
     *
     * @return the list of player results.
     */
    public List<PlayerResultDTO> getPlayerResults() {
        return playerResults;
    }

    /**
     * Sets the list of player results for the tournament.
     *
     * @param playerResults the list of player results to set.
     */
    public void setPlayerResults(List<PlayerResultDTO> playerResults) {
        this.playerResults = playerResults;
    }

    /**
     * Nested Data Transfer Object (DTO) to represent an individual player's result.
     * This includes the player's ID and the points they earned.
     */
    public static class PlayerResultDTO {

        /**
         * The unique identifier of the player.
         */
        private Long playerId;

        /**
         * The number of points the player earned in the tournament.
         */
        private Double points;

        /**
         * Constructor to initialize the player result with player ID and points.
         *
         * @param playerId the ID of the player.
         * @param points the points the player earned.
         */
        public PlayerResultDTO(Long playerId, Double points) {
            this.playerId = playerId;
            this.points = points;
        }

        /**
         * Gets the ID of the player.
         *
         * @return the player ID.
         */
        public Long getPlayerId() {
            return playerId;
        }

        /**
         * Sets the ID of the player.
         *
         * @param playerId the ID to set for the player.
         */
        public void setPlayerId(Long playerId) {
            this.playerId = playerId;
        }

        /**
         * Gets the points earned by the player.
         *
         * @return the points earned by the player.
         */
        public Double getPoints() {
            return points;
        }

        /**
         * Sets the points earned by the player.
         *
         * @param points the points to set for the player.
         */
        public void setPoints(Double points) {
            this.points = points;
        }
    }
}

