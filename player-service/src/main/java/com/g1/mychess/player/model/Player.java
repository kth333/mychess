package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    @NotNull
    @Column(name = "role")
    private String role = "ROLE_PLAYER";

    @Column(name = "is_blacklisted", nullable = false)
    private boolean isBlacklisted = false;

    @NotNull
    @Column(name = "username", nullable = false, unique = true, length = 16)
    private String username;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Email(message = "Email should be valid.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @NotNull
    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @NotNull
    @Column(name = "tournament_count")
    private Integer tournamentCount;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlayerRatingHistory> ratingHistory;

    // Getters and Setters

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    public Integer getTournamentCount() {
        return tournamentCount;
    }

    public void setTournamentCount(Integer tournamentCount) {
        this.tournamentCount = tournamentCount;
    }

    public List<PlayerRatingHistory> getRatingHistory() {
        return ratingHistory;
    }

    public void setRatingHistory(List<PlayerRatingHistory> ratingHistory) {
        this.ratingHistory = ratingHistory;
    }
}
