package com.g1.mychess.tournament.model;

import com.g1.mychess.user.model.Player;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_player")
public class TournamentPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "sign_up_date_time", nullable = false)
    private LocalDateTime signUpDateTime;

    @Column(name = "score")
    private Integer score;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TournamentPlayerStatus status;

    public enum TournamentPlayerStatus {
        ACTIVE,         // Currently participating
        ELIMINATED,     // Eliminated from the tournament
        WINNER,         // Winner of the tournament
        RUNNER_UP,      // Finished in second place
        THIRD_PLACE,    // Finished in third place
        DISQUALIFIED,   // Disqualified from the tournament
        WITHDRAWN,      // Voluntarily withdrew or retired from the tournament
        FINALIST        // Reached the final round
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getSignUpDateTime() {
        return signUpDateTime;
    }

    public void setSignUpDateTime(LocalDateTime signUpDateTime) {
        this.signUpDateTime = signUpDateTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public TournamentPlayerStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentPlayerStatus status) {
        this.status = status;
    }
}
