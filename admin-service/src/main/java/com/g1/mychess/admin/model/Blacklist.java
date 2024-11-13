package com.g1.mychess.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

/**
 * Represents a record in the blacklist table for a player.
 * This entity is used to manage players who have been banned, including details
 * such as the reason for the ban, the admin responsible for the ban, the ban duration,
 * and when the player is allowed to be whitelisted again.
 */
@Entity
@Table(name = "blacklists")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the player who has been blacklisted.
     * This field cannot be null.
     */
    @NotNull(message = "Player ID cannot be null")
    @Column(name = "player_id", nullable = false)
    private Long playerId;

    /**
     * The ID of the admin who blacklisted the player.
     * This field cannot be null.
     */
    @Column(name = "admin_id")
    @NotNull
    private Long adminId;

    /**
     * The reason for blacklisting the player.
     * This field has a maximum length of 255 characters.
     */
    @Size(max = 255, message = "Reason cannot exceed 255 characters")
    @Column(name = "reason")
    private String reason;

    /**
     * The timestamp of when the player was blacklisted.
     * This field cannot be null.
     */
    @NotNull(message = "Blacklisted time cannot be null")
    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    /**
     * The timestamp of when the player can be whitelisted again.
     * This value is calculated based on the ban duration and blacklisting time.
     */
    @Column(name = "whitelisted_at")
    @NotNull
    private LocalDateTime whitelistedAt;

    /**
     * The duration of the ban in hours.
     * This field cannot be null.
     */
    @NotNull(message = "Ban duration cannot be null")
    @Column(name = "ban_duration", nullable = false)
    private Long banDuration;

    /**
     * Indicates whether the player is still banned.
     * This field cannot be null.
     */
    @NotNull(message = "Active status cannot be null")
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * This method is automatically called before the entity is persisted or updated.
     * It calculates the `whitelistedAt` timestamp based on the `blacklistedAt` timestamp and `banDuration`.
     * If `whitelistedAt` is not provided, it is calculated as `blacklistedAt + banDuration`.
     */
    @PrePersist
    @PreUpdate
    private void calculateWhitelistedAt() {
        if (whitelistedAt == null && blacklistedAt != null && banDuration != null) {
            this.whitelistedAt = blacklistedAt.plusHours(banDuration);
        }
    }

    // Getters and Setters
    /**
     * Gets the unique ID of the blacklist record.
     * @return the ID of the blacklist record
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the blacklist record.
     * @param id the ID to set for the blacklist record
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the player who is blacklisted.
     * @return the player ID
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the ID of the player who is blacklisted.
     * @param playerId the player ID to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the ID of the admin who blacklisted the player.
     * @return the admin ID
     */
    public Long getAdminId() {
        return adminId;
    }

    /**
     * Sets the ID of the admin who blacklisted the player.
     * @param adminId the admin ID to set
     */
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    /**
     * Gets the reason for blacklisting the player.
     * @return the reason for the blacklist
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets the reason for blacklisting the player.
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the timestamp of when the player was blacklisted.
     * @return the timestamp of blacklisting
     */
    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }

    /**
     * Sets the timestamp of when the player was blacklisted.
     * @param blacklistedAt the blacklisting timestamp to set
     */
    public void setBlacklistedAt(LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }

    /**
     * Gets the timestamp of when the player can be whitelisted again.
     * @return the whitelisted timestamp
     */
    public LocalDateTime getWhitelistedAt() {
        return whitelistedAt;
    }

    /**
     * Sets the timestamp of when the player can be whitelisted again.
     * @param whitelistedAt the timestamp to set
     */
    public void setWhitelistedAt(LocalDateTime whitelistedAt) {
        this.whitelistedAt = whitelistedAt;
    }

    /**
     * Gets the duration of the ban in hours.
     * @return the ban duration in hours
     */
    public Long getBanDuration() {
        return banDuration;
    }

    /**
     * Sets the duration of the ban in hours.
     * @param banDuration the ban duration to set
     */
    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }

    /**
     * Gets the active status of the blacklist.
     * @return true if the player is still banned, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active status of the blacklist.
     * @param active true if the player is still banned, false otherwise
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}
