package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @Column(name = "player_id")
    private Long playerId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters.")
    @Column(name = "full_name")
    private String fullName;

    @Size(max = 500, message = "Bio must be less than 500 characters.")
    @Column(name = "bio")
    private String bio;

    @Pattern(regexp = "(https?|ftp)://.*", message = "Avatar URL must be a valid URL.")
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "gender")
    private String gender;

    @NotNull
    @Column(name = "country")
    private String country;

    @NotNull
    @Size(max = 100, message = "Region must be less than 100 characters.")
    @Column(name = "region")
    private String region;

    @NotNull
    @Size(max = 100, message = "City must be less than 100 characters.")
    @Column(name = "city")
    private String city;

    @NotNull
    @Past(message = "Birth date must be in the past.")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "chess_rank")
    private CustomChessRank rank;

    @Column(name = "glicko_rating", nullable = false)
    @Min(value = 0, message = "Glicko rating must be non-negative.")
    private double glickoRating = 1500;

    @Column(name = "rating_deviation", nullable = false)
    @Min(value = 30, message = "Rating deviation must be at least 30.")
    private double ratingDeviation = 350.0;

    @Column(name = "volatility", nullable = false)
    @Min(value = 0, message = "Volatility must be non-negative.")
    private double volatility = 0.06;

    @Column(name = "total_wins")
    @Min(value = 0, message = "Total wins must be non-negative.")
    private int totalWins;

    @Column(name = "total_losses")
    @Min(value = 0, message = "Total losses must be non-negative.")
    private int totalLosses;

    @Column(name = "total_draws")
    @Min(value = 0, message = "Total draws must be non-negative.")
    private int totalDraws;

    @NotNull
    @Column(name = "last_active")
    private LocalDate lastActive;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchHistory> matchHistory = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TournamentHistory> tournamentHistory = new ArrayList<>();

    @Column(name = "is_public")
    private boolean isPublic;


    // Getters and Setters

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
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

    public String getGender() { return gender; }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = country; }

    public String getRegion() { return region; }

    public void setRegion(String region) { this.region = region; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

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

    public double getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    public double getRatingDeviation() {
        return ratingDeviation;
    }

    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
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

    public LocalDate getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDate lastActive) {
        this.lastActive = lastActive;
    }

    public List<MatchHistory> getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistory(List<MatchHistory> matchHistory) {
        this.matchHistory = matchHistory;
    }

    public List<TournamentHistory> getTournamentHistory() {
        return tournamentHistory;
    }

    public void setTournamentHistory(List<TournamentHistory> tournamentHistory) {
        this.tournamentHistory = tournamentHistory;
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

    @Override
    public String toString() {
        return "Profile{" +
                "playerId=" + playerId +
                ", player=" + (player != null ? player.getPlayerId() : "null") +
                ", fullName='" + fullName + '\'' +
                ", bio='" + bio + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", birthDate=" + birthDate +
                ", age=" + getAge() +
                ", rank=" + rank +
                ", glickoRating=" + glickoRating +
                ", ratingDeviation=" + ratingDeviation +
                ", volatility=" + volatility +
                ", totalWins=" + totalWins +
                ", totalLosses=" + totalLosses +
                ", totalDraws=" + totalDraws +
                ", lastActive=" + lastActive +
                ", matchHistory=" + matchHistory +
                ", tournamentHistory=" + tournamentHistory +
                ", isPublic=" + isPublic +
                '}';
    }
}
