package com.g1.mychess.match.dto;

import java.util.List;

public class TournamentResultsDTO {

    private Long tournamentId;
    private List<PlayerResultDTO> playerResults;

    // Constructor
    public TournamentResultsDTO(Long tournamentId, List<PlayerResultDTO> playerResults) {
        this.tournamentId = tournamentId;
        this.playerResults = playerResults;
    }

    // Getters and Setters
    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public List<PlayerResultDTO> getPlayerResults() {
        return playerResults;
    }

    public void setPlayerResults(List<PlayerResultDTO> playerResults) {
        this.playerResults = playerResults;
    }

    // Nested DTO to represent player results
    public static class PlayerResultDTO {
        private Long playerId;
        private Double points;
    
        // Constructor
        public PlayerResultDTO(Long playerId, Double points) {
            this.playerId = playerId;
            this.points = points;
        }
    
        // Getters and Setters
        public Long getPlayerId() {
            return playerId;
        }
    
        public void setPlayerId(Long playerId) {
            this.playerId = playerId;
        }
    
        public Double getPoints() {
            return points;
        }
    
        public void setPoints(Double points) {
            this.points = points;
        }
    }
    
}
