package com.g1.mychess.match.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;

/**
 * Data Transfer Object (DTO) for representing match information in a tournament.
 * This class is used to transfer match data between layers of the application.
 */
public class MatchDTO {

    /**
     * The unique identifier for the match.
     */
    private Long id;

    /**
     * The unique identifier for the tournament this match belongs to.
     */
    private Long tournamentId;

    /**
     * A set of participant IDs for the match. Each participant is represented by their unique player ID.
     */
    private Set<Long> participantIds;  // Store participant IDs directly

    /**
     * The scheduled date and time for the match.
     */
    private LocalDateTime scheduledTime;

    /**
     * The status of the match (e.g., ongoing, completed, etc.).
     */
    private Match.MatchStatus status;

    /**
     * The round number in the tournament that this match belongs to.
     */
    private Integer roundNumber;

    // Constructors

    /**
     * Default constructor.
     */
    public MatchDTO() {}

    /**
     * Constructs a MatchDTO object with the specified match details.
     *
     * @param id The unique identifier for the match.
     * @param tournamentId The unique identifier for the tournament.
     * @param participantIds A set of participant IDs in the match.
     * @param scheduledTime The scheduled time for the match.
     * @param status The status of the match.
     * @param roundNumber The round number of the match in the tournament.
     */
    public MatchDTO(Long id, Long tournamentId, Set<Long> participantIds,
                    LocalDateTime scheduledTime, Match.MatchStatus status, Integer roundNumber) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.participantIds = participantIds;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.roundNumber = roundNumber;
    }

    // Static Factory Method to Convert Match Entity to DTO

    /**
     * Converts a `Match` entity to a `MatchDTO` object.
     *
     * @param match The match entity to convert.
     * @return A `MatchDTO` object containing the match details.
     */
    public static MatchDTO fromEntity(Match match) {
        Set<Long> participantIds = match.getParticipants()
            .stream()
            .map(MatchPlayer::getPlayerId)
            .collect(Collectors.toSet());

        return new MatchDTO(
            match.getId(),
            match.getTournamentId(),
            participantIds,
            match.getScheduledTime(),
            match.getStatus(),
            match.getRoundNumber()
        );
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the match.
     *
     * @return The match ID.
     */
    public Long getId() { return id; }

    /**
     * Sets the unique identifier for the match.
     *
     * @param id The match ID to set.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Gets the unique identifier for the tournament this match belongs to.
     *
     * @return The tournament ID.
     */
    public Long getTournamentId() { return tournamentId; }

    /**
     * Sets the unique identifier for the tournament.
     *
     * @param tournamentId The tournament ID to set.
     */
    public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }

    /**
     * Gets the set of participant IDs for the match.
     *
     * @return A set of participant IDs.
     */
    public Set<Long> getParticipantIds() { return participantIds; }

    /**
     * Sets the set of participant IDs for the match.
     *
     * @param participantIds The set of participant IDs to set.
     */
    public void setParticipantIds(Set<Long> participantIds) { this.participantIds = participantIds; }

    /**
     * Gets the scheduled time for the match.
     *
     * @return The scheduled time of the match.
     */
    public LocalDateTime getScheduledTime() { return scheduledTime; }

    /**
     * Sets the scheduled time for the match.
     *
     * @param scheduledTime The scheduled time to set.
     */
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    /**
     * Gets the status of the match.
     *
     * @return The status of the match.
     */
    public Match.MatchStatus getStatus() { return status; }

    /**
     * Sets the status of the match.
     *
     * @param status The status to set.
     */
    public void setStatus(Match.MatchStatus status) { this.status = status; }

    /**
     * Gets the round number of the match in the tournament.
     *
     * @return The round number of the match.
     */
    public Integer getRoundNumber() { return roundNumber; }

    /**
     * Sets the round number of the match in the tournament.
     *
     * @param roundNumber The round number to set.
     */
    public void setRoundNumber(Integer roundNumber) { this.roundNumber = roundNumber; }
}