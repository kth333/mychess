package com.g1.mychess.admin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklists")
public class Blacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "whitelisted_at")
    private LocalDateTime whitelistedAt;

    @Column(name = "ban_duration", nullable = false)
    private Long banDuration;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @PrePersist
    @PreUpdate
    private void calculateWhitelistedAt() {
        if (whitelistedAt == null && blacklistedAt != null && banDuration != null) {
            this.whitelistedAt = blacklistedAt.plusHours(banDuration);
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getBlacklistedAt() {
        return blacklistedAt;
    }

    public void setBlacklistedAt(LocalDateTime blacklistedAt) {
        this.blacklistedAt = blacklistedAt;
    }

    public LocalDateTime getWhitelistedAt() {
        return whitelistedAt;
    }

    public void setWhitelistedAt(LocalDateTime whitelistedAt) {
        this.whitelistedAt = whitelistedAt;
    }

    public Long getBanDuration() {
        return banDuration;
    }

    public void setBanDuration(Long banDuration) {
        this.banDuration = banDuration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
