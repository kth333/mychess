package com.g1.mychess.match.model;

import com.g1.mychess.tournament.model.Tournament;
import com.g1.mychess.user.model.Player;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

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

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

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
