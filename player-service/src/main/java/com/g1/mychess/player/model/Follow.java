package com.g1.mychess.player.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Represents a follow relationship between two players.
 * A player can follow another player, and this entity records the details of the follow event.
 */
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

    /**
     * Default constructor for Follow.
     */
    public Follow() {}

    /**
     * Constructor to initialize a follow relationship between two players.
     *
     * @param follower the player who is following another player.
     * @param followed the player being followed.
     */
    public Follow(Player follower, Player followed) {
        this.follower = follower;
        this.followed = followed;
        this.followDate = LocalDateTime.now();
    }

    // Getters and Setters

    /**
     * Gets the ID of the follow record.
     *
     * @return the ID of the follow record.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the follow record.
     *
     * @param id the new ID to assign to the follow record.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the player who is following another player.
     *
     * @return the player who is following another player.
     */
    public Player getFollower() {
        return follower;
    }

    /**
     * Sets the player who is following another player.
     *
     * @param follower the new player who is following another player.
     */
    public void setFollower(Player follower) {
        this.follower = follower;
    }

    /**
     * Gets the player who is being followed.
     *
     * @return the player being followed.
     */
    public Player getFollowed() {
        return followed;
    }

    /**
     * Sets the player who is being followed.
     *
     * @param followed the new player being followed.
     */
    public void setFollowed(Player followed) {
        this.followed = followed;
    }

    /**
     * Gets the date when the follow action occurred.
     *
     * @return the date and time when the follow action took place.
     */
    public LocalDateTime getFollowDate() {
        return followDate;
    }

    /**
     * Sets the date when the follow action occurred.
     *
     * @param followDate the new date and time when the follow action occurred.
     */
    public void setFollowDate(LocalDateTime followDate) {
        this.followDate = followDate;
    }
}
