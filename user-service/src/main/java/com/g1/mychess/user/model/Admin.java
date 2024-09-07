package com.g1.mychess.user.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {

    @Column(name = "managed_tournament_count")
    private Integer managedTournamentCount;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public Admin() {
        super();
        this.setRole("PLAYER");
    }

    // Getters and Setters

    public Integer getManagedTournamentCount() {
        return managedTournamentCount;
    }

    public void setManagedTournamentCount(Integer managedTournamentCount) {
        this.managedTournamentCount = managedTournamentCount;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
