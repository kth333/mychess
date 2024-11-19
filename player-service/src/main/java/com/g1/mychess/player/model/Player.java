package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a player in the chess system. Each player has personal details,
 * a role, a blacklist status, and a profile associated with their account.
 * This entity is mapped to the "players" table in the database.
 */
@Entity
@Table(name = "players")
public class Player {

    /**
     * The unique identifier for the player.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_id")
    private Long playerId;

    /**
     * The role assigned to the player, defaults to "ROLE_PLAYER".
     */
    @NotNull
    @Column(name = "role")
    private String role = "ROLE_PLAYER";

    /**
     * Indicates whether the player is blacklisted.
     * Blacklisted players may have restricted access.
     */
    @Column(name = "is_blacklisted", nullable = false)
    private boolean isBlacklisted = false;

    /**
     * The username of the player. It must be unique and limited to 16 characters.
     */
    @NotNull
    @Column(name = "username", nullable = false, unique = true, length = 16)
    private String username;

    /**
     * The password of the player. It is stored securely in the database.
     */
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * The email address of the player. It must be unique and valid.
     */
    @NotNull
    @Email(message = "Email should be valid.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * The profile associated with the player. This relationship is one-to-one.
     */
    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    /**
     * The date when the player joined the system.
     */
    @NotNull
    @Column(name = "joined_date")
    private LocalDate joinedDate;

    /**
     * The number of tournaments the player has participated in.
     */
    @NotNull
    @Column(name = "tournament_count")
    private Integer tournamentCount;

    /**
     * The rating history of the player. This relationship is one-to-many.
     */
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PlayerRatingHistory> ratingHistory;


    // Getters and Setters

    /**
     * Gets the player's unique identifier.
     *
     * @return the player's ID.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerId the new player ID.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player's role.
     *
     * @return the player's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the player's role.
     *
     * @param role the new role to assign to the player.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Checks if the player is blacklisted.
     *
     * @return true if the player is blacklisted, false otherwise.
     */
    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    /**
     * Sets the player's blacklist status.
     *
     * @param blacklisted the new blacklist status.
     */
    public void setBlacklisted(boolean blacklisted) {
        isBlacklisted = blacklisted;
    }

    /**
     * Gets the player's username.
     *
     * @return the player's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username the new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the player's password.
     *
     * @return the player's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the player's password.
     *
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the player's email address.
     *
     * @return the player's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the player's email address.
     *
     * @param email the new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the player's profile.
     *
     * @return the profile associated with the player.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Sets the player's profile.
     *
     * @param profile the new profile to associate with the player.
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * Gets the player's join date.
     *
     * @return the date when the player joined the system.
     */
    public LocalDate getJoinedDate() {
        return joinedDate;
    }

    /**
     * Sets the player's join date.
     *
     * @param joinedDate the new join date.
     */
    public void setJoinedDate(LocalDate joinedDate) {
        this.joinedDate = joinedDate;
    }

    /**
     * Gets the number of tournaments the player has participated in.
     *
     * @return the tournament count.
     */
    public Integer getTournamentCount() {
        return tournamentCount;
    }

    /**
     * Sets the number of tournaments the player has participated in.
     *
     * @param tournamentCount the new tournament count.
     */
    public void setTournamentCount(Integer tournamentCount) {
        this.tournamentCount = tournamentCount;
    }

    /**
     * Gets the player's rating history.
     *
     * @return the list of the player's rating history records.
     */
    public List<PlayerRatingHistory> getRatingHistory() {
        return ratingHistory;
    }

    /**
     * Sets the player's rating history.
     *
     * @param ratingHistory the new rating history records.
     */
    public void setRatingHistory(List<PlayerRatingHistory> ratingHistory) {
        this.ratingHistory = ratingHistory;
    }
}
