package com.g1.mychess.player.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Player follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private Player followed;

    private LocalDateTime followDate;

    // Constructors, getters, setters
    public Follow() {}

    public Follow(Player follower, Player followed) {
        this.follower = follower;
        this.followed = followed;
        this.followDate = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getFollower() {
        return follower;
    }

    public void setFollower(Player follower) {
        this.follower = follower;
    }

    public Player getFollowed() {
        return followed;
    }

    public void setFollowed(Player followed) {
        this.followed = followed;
    }

    public LocalDateTime getFollowDate() {
        return followDate;
    }

    public void setFollowDate(LocalDateTime followDate) {
        this.followDate = followDate;
    }
}
