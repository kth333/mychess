package com.g1.mychess.user.model;

import com.g1.mychess.profile.model.Profile;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "players")
@PrimaryKeyJoinColumn(name = "user_id")
public class Player extends User {

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL)
    private Profile profile;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Column(name = "tournament_count")
    private Integer tournamentCount;

    @Column(name = "is_active")
    private Boolean isActive;

    public Player() {
        super();
        this.setRole(Role.PLAYER);
    }

    // Getters and Setters

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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
