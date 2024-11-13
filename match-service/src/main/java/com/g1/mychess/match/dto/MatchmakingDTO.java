package com.g1.mychess.match.dto;

import java.util.Set;

/**
 * Data Transfer Object (DTO) for representing the matchmaking information in a tournament.
 * This class contains the tournament ID, current round, maximum rounds, and the list of participants.
 */
public class MatchmakingDTO {

    /**
     * The unique identifier of the tournament for which matchmaking is being done.
     */
    private Long tournamentId;

    /**
     * The current round in the tournament.
     */
    private int currentRound;

    /**
     * The maximum number of rounds for the tournament.
     */
    private int maxRounds;

    /**
     * A set of participants in the tournament. Each participant's information is represented by
     * the `TournamentPlayerDTO` class.
     */
    private Set<TournamentPlayerDTO> participants;  // Contains participants' information

    // Constructor

    /**
     * Constructs a MatchmakingDTO object with the specified tournament ID, current round,
     * maximum rounds, and set of participants.
     *
     * @param tournamentId The unique identifier for the tournament.
     * @param currentRound The current round of the tournament.
     * @param maxRounds The maximum rounds allowed for the tournament.
     * @param participants A set containing the participants of the tournament.
     */
    public MatchmakingDTO(Long tournamentId, int currentRound, int maxRounds, Set<TournamentPlayerDTO> participants) {
        this.tournamentId = tournamentId;
        this.currentRound = currentRound;
        this.maxRounds = maxRounds;
        this.participants = participants;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the tournament.
     *
     * @return The tournament ID.
     */
    public Long getTournamentId() {
        return tournamentId;
    }

    /**
     * Sets the unique identifier for the tournament.
     *
     * @param tournamentId The tournament ID to set.
     */
    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    /**
     * Gets the current round of the tournament.
     *
     * @return The current round.
     */
    public int getCurrentRound() {
        return currentRound;
    }

    /**
     * Sets the current round of the tournament.
     *
     * @param currentRound The current round to set.
     */
    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    /**
     * Gets the maximum number of rounds in the tournament.
     *
     * @return The maximum rounds.
     */
    public int getMaxRounds() {
        return maxRounds;
    }

    /**
     * Sets the maximum number of rounds in the tournament.
     *
     * @param maxRounds The maximum rounds to set.
     */
    public void setMaxRounds(int maxRounds) {
        this.maxRounds = maxRounds;
    }

    /**
     * Gets the set of participants in the tournament.
     *
     * @return A set of `TournamentPlayerDTO` objects representing the participants.
     */
    public Set<TournamentPlayerDTO> getParticipants() {
        return participants;
    }

    /**
     * Sets the set of participants in the tournament.
     *
     * @param participants The set of participants to set.
     */
    public void setParticipants(Set<TournamentPlayerDTO> participants) {
        this.participants = participants;
    }
}