package com.g1.mychess.user.model;

import com.g1.mychess.common.enums.CustomChessRank;
import com.g1.mychess.common.enums.Gender;
import com.g1.mychess.location.model.Location;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Player player;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "chess_rank")
    private CustomChessRank rank;

    @Column(name = "total_wins")
    private int totalWins;

    @Column(name = "total_losses")
    private int totalLosses;

    @Column(name = "total_draws")
    private int totalDraws;

    @Column(name = "elo_rating")
    private int eloRating;

    @Column(name = "glicko_rating")
    private int glickoRating;

    @Column(name = "rating_deviation")
    private int ratingDeviation;

    @Column(name = "is_public")
    private boolean isPublic;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public CustomChessRank getRank() {
        return rank;
    }

    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public int getTotalDraws() {
        return totalDraws;
    }

    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
    }

    public int getEloRating() {
        return eloRating;
    }

    public void setEloRating(int eloRating) {
        this.eloRating = eloRating;
    }

    public int getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(int glickoRating) {
        this.glickoRating = glickoRating;
    }

    public int getRatingDeviation() {
        return ratingDeviation;
    }

    public void setRatingDeviation(int ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }
}
