package com.g1.mychess.match.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tournament_id", nullable = false)
    private Long tournamentId;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchPlayer> participants;

    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MatchStatus status;

    @Column(name = "round_number")
    private Integer roundNumber;

    public enum MatchStatus {
        SCHEDULED,
        ONGOING,
        COMPLETED,
        CANCELED
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTournamentId() { return tournamentId; }

    public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }

    public Set<MatchPlayer> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<MatchPlayer> participants) {
        this.participants = participants;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }
}
