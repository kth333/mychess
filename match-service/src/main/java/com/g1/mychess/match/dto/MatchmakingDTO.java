package com.g1.mychess.match.dto;

import java.util.Set;

public class MatchmakingDTO {
    private Long tournamentId;
    private int currentRound;
    private int maxRounds;
    private Set<TournamentPlayerDTO> participants;  // Contains participants info

    // Constructor
    public MatchmakingDTO(Long tournamentId, int currentRound, int maxRounds, Set<TournamentPlayerDTO> participants) {
        this.tournamentId = tournamentId;
        this.currentRound = currentRound;
        this.maxRounds = maxRounds;
        this.participants = participants;
    }

    // Getters and setters
    public Long getTournamentId() {
        return tournamentId;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public Set<TournamentPlayerDTO> getParticipants() {
        return participants;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    public void setParticipants(Set<TournamentPlayerDTO> participants) {
        this.participants = participants;
    }
}

