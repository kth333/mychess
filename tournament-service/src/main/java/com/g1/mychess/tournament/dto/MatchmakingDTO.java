package com.g1.mychess.tournament.dto;

import java.util.Set;

/**
 * Data Transfer Object for matchmaking operations in a tournament.
 */
public class MatchmakingDTO {

    private Long tournamentId;
    private int currentRound;
    private int maxRounds;
    private Set<TournamentPlayerDTO> participants; // Contains participants info

    /**
     * Constructs a new {@code MatchmakingDTO} with the specified details.
     *
     * @param tournamentId the ID of the tournament
     * @param currentRound the current round of the tournament
     * @param maxRounds    the maximum number of rounds in the tournament
     * @param participants the participants in the tournament
     */
    public MatchmakingDTO(Long tournamentId, int currentRound, int maxRounds, Set<TournamentPlayerDTO> participants) {
        this.tournamentId = tournamentId;
        this.currentRound = currentRound;
        this.maxRounds = maxRounds;
        this.participants = participants;
    }

    /**
     * Returns the tournament ID.
     *
     * @return the tournament ID
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the tournament ID.
     *
     * @param tournamentId the tournament ID
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Returns the current round of the tournament.
     *
     * @return the current round
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Sets the current round of the tournament.
     *
     * @param currentRound the current round
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Returns the maximum number of rounds in the tournament.
     *
     * @return the maximum number of rounds
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * Sets the maximum number of rounds in the tournament.
     *
     * @param maxRounds the maximum number of rounds
     */
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    /**
     * Returns the participants in the tournament.
     *
     * @return the participants
     */
    public Set<TournamentPlayerDTO> getParticipants() {
        return participants;
    }

    /**
     * Sets the participants in the tournament.
     *
     * @param participants the participants
     */
    public void setParticipants(Set<TournamentPlayerDTO> participants) {
        this.participants = participants;
    }
}
