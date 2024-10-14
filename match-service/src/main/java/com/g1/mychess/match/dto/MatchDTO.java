package com.g1.mychess.match.dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import com.g1.mychess.match.model.Match;
import com.g1.mychess.match.model.MatchPlayer;

public class MatchDTO {

    private Long id;
    private Long tournamentId;
    private Set<Long> participantIds;  // Store participant IDs directly
    private LocalDateTime scheduledTime;
    private Match.MatchStatus status;
    private Integer roundNumber;

    // Constructors
    public MatchDTO() {}

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
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTournamentId() { return tournamentId; }
    public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }

    public Set<Long> getParticipantIds() { return participantIds; }
    public void setParticipantIds(Set<Long> participantIds) { this.participantIds = participantIds; }

    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }

    public Match.MatchStatus getStatus() { return status; }
    public void setStatus(Match.MatchStatus status) { this.status = status; }

    public Integer getRoundNumber() { return roundNumber; }
    public void setRoundNumber(Integer roundNumber) { this.roundNumber = roundNumber; }
}
