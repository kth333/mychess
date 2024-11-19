package com.g1.mychess.player.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's profile in the system.
 * This entity stores information such as personal details, chess rank, match history, and tournament history.
 * It is associated with the {@link Player} entity through a one-to-one relationship.
 */
@Entity
@Table(name = "profiles")
public class Profile {

    /**
     * Unique identifier for the player.
     * This is a foreign key that maps to the {@link Player} entity.
     */
    @Id
    @Column(name = "player_id")
    private Long playerId;

    /**
     * The player associated with this profile.
     * It is mapped by the {@link Player#profile} property.
     */
    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id")
    private Player player;

    /**
     * Full name of the player.
     * It must be between 2 and 100 characters.
     */
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters.")
    @Column(name = "full_name")
    private String fullName;

    /**
     * Bio of the player.
     * It must be less than 500 characters.
     */
    @Size(max = 500, message = "Bio must be less than 500 characters.")
    @Column(name = "bio")
    private String bio;

    /**
     * Avatar URL of the player.
     * It must be a valid URL.
     */
    @Pattern(regexp = "(https?|ftp)://.*", message = "Avatar URL must be a valid URL.")
    @Column(name = "avatar_url")
    private String avatarUrl;

    /**
     * Gender of the player.
     * This is an optional field.
     */
    @Column(name = "gender")
    private String gender;

    /**
     * Country of residence of the player.
     * This is a required field.
     */
    @NotNull
    @Column(name = "country")
    private String country;

    /**
     * Region of residence of the player.
     * This is a required field and must be less than 100 characters.
     */
    @NotNull
    @Size(max = 100, message = "Region must be less than 100 characters.")
    @Column(name = "region")
    private String region;

    /**
     * City of residence of the player.
     * This is a required field and must be less than 100 characters.
     */
    @NotNull
    @Size(max = 100, message = "City must be less than 100 characters.")
    @Column(name = "city")
    private String city;

    /**
     * Birth date of the player.
     * This is a required field and must be in the past.
     */
    @NotNull
    @Past(message = "Birth date must be in the past.")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /**
     * Chess rank of the player.
     * This field uses a custom enumeration {@link CustomChessRank}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "chess_rank")
    private CustomChessRank rank;

    /**
     * Glicko rating of the player.
     * This is a required field and must be non-negative. The default value is 1500.
     */
    @Column(name = "glicko_rating", nullable = false)
    @Min(value = 0, message = "Glicko rating must be non-negative.")
    private double glickoRating = 1500;

    /**
     * Rating deviation of the player.
     * This is a required field and must be at least 30. The default value is 350.0.
     */
    @Column(name = "rating_deviation", nullable = false)
    @Min(value = 30, message = "Rating deviation must be at least 30.")
    private double ratingDeviation = 350.0;

    /**
     * Volatility of the player's rating.
     * This is a required field and must be non-negative. The default value is 0.06.
     */
    @Column(name = "volatility", nullable = false)
    @Min(value = 0, message = "Volatility must be non-negative.")
    private double volatility = 0.06;

    /**
     * Total number of wins the player has.
     * This is a required field and must be non-negative.
     */
    @Column(name = "total_wins")
    @Min(value = 0, message = "Total wins must be non-negative.")
    private int totalWins;

    /**
     * Total number of losses the player has.
     * This is a required field and must be non-negative.
     */
    @Column(name = "total_losses")
    @Min(value = 0, message = "Total losses must be non-negative.")
    private int totalLosses;

    /**
     * Total number of draws the player has.
     * This is a required field and must be non-negative.
     */
    @Column(name = "total_draws")
    @Min(value = 0, message = "Total draws must be non-negative.")
    private int totalDraws;

    /**
     * The last date when the player was active.
     * This is a required field.
     */
    @NotNull
    @Column(name = "last_active")
    private LocalDate lastActive;

    /**
     * The list of matches the player has participated in.
     * This field represents a one-to-many relationship with {@link MatchHistory}.
     */
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchHistory> matchHistory = new ArrayList<>();

    /**
     * The list of tournaments the player has participated in.
     * This field represents a one-to-many relationship with {@link TournamentHistory}.
     */
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TournamentHistory> tournamentHistory = new ArrayList<>();

    /**
     * Indicates whether the profile is public or private.
     */
    @Column(name = "is_public")
    private boolean isPublic;


    // Getters and Setters

    /**
     * Gets the player's unique identifier.
     *
     * @return the player ID.
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Sets the player's unique identifier.
     *
     * @param playerId the player ID to set.
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /**
     * Gets the player associated with this profile.
     *
     * @return the player object.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player associated with this profile.
     *
     * @param player the player object to set.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Gets the full name of the player.
     *
     * @return the player's full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the full name of the player.
     *
     * @param fullName the full name to set.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets the bio of the player.
     *
     * @return the player's bio.
     */
    public String getBio() {
        return bio;
    }

    /**
     * Sets the bio of the player.
     *
     * @param bio the bio to set.
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * Gets the URL of the player's avatar.
     *
     * @return the avatar URL.
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * Sets the URL of the player's avatar.
     *
     * @param avatarUrl the avatar URL to set.
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    /**
     * Gets the gender of the player.
     *
     * @return the player's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the player.
     *
     * @param gender the gender to set.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the country of the player.
     *
     * @return the player's country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country of the player.
     *
     * @param country the country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the region of the player.
     *
     * @return the player's region.
     */
    public String getRegion() {
        return region;
    }

    /**
     * Sets the region of the player.
     *
     * @param region the region to set.
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Gets the city of the player.
     *
     * @return the player's city.
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the player.
     *
     * @param city the city to set.
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Gets the birth date of the player.
     *
     * @return the player's birth date.
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the birth date of the player.
     *
     * @param birthDate the birth date to set.
     */
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the custom chess rank of the player.
     *
     * @return the player's rank.
     */
    public CustomChessRank getRank() {
        return rank;
    }

    /**
     * Sets the custom chess rank of the player.
     *
     * @param rank the rank to set.
     */
    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    /**
     * Gets the Glicko rating of the player.
     *
     * @return the player's Glicko rating.
     */
    public double getGlickoRating() {
        return glickoRating;
    }

    /**
     * Sets the Glicko rating of the player.
     *
     * @param glickoRating the Glicko rating to set.
     */
    public void setGlickoRating(double glickoRating) {
        this.glickoRating = glickoRating;
    }

    /**
     * Gets the rating deviation of the player.
     *
     * @return the player's rating deviation.
     */
    public double getRatingDeviation() {
        return ratingDeviation;
    }

    /**
     * Sets the rating deviation of the player.
     *
     * @param ratingDeviation the rating deviation to set.
     */
    public void setRatingDeviation(double ratingDeviation) {
        this.ratingDeviation = ratingDeviation;
    }

    /**
     * Gets the volatility of the player's rating.
     *
     * @return the player's rating volatility.
     */
    public double getVolatility() {
        return volatility;
    }

    /**
     * Sets the volatility of the player's rating.
     *
     * @param volatility the rating volatility to set.
     */
    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    /**
     * Gets the total number of wins for the player.
     *
     * @return the player's total wins.
     */
    public int getTotalWins() {
        return totalWins;
    }

    /**
     * Sets the total number of wins for the player.
     *
     * @param totalWins the total wins to set.
     */
    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    /**
     * Gets the total number of losses for the player.
     *
     * @return the player's total losses.
     */
    public int getTotalLosses() {
        return totalLosses;
    }

    /**
     * Sets the total number of losses for the player.
     *
     * @param totalLosses the total losses to set.
     */
    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    /**
     * Gets the total number of draws for the player.
     *
     * @return the player's total draws.
     */
    public int getTotalDraws() {
        return totalDraws;
    }

    /**
     * Sets the total number of draws for the player.
     *
     * @param totalDraws the total draws to set.
     */
    public void setTotalDraws(int totalDraws) {
        this.totalDraws = totalDraws;
    }

    /**
     * Gets the last active date of the player.
     *
     * @return the player's last active date.
     */
    public LocalDate getLastActive() {
        return lastActive;
    }

    /**
     * Sets the last active date of the player.
     *
     * @param lastActive the last active date to set.
     */
    public void setLastActive(LocalDate lastActive) {
        this.lastActive = lastActive;
    }

    /**
     * Gets the match history of the player.
     *
     * @return the player's match history.
     */
    public List<MatchHistory> getMatchHistory() {
        return matchHistory;
    }

    /**
     * Sets the match history of the player.
     *
     * @param matchHistory the match history to set.
     */
    public void setMatchHistory(List<MatchHistory> matchHistory) {
        this.matchHistory = matchHistory;
    }

    /**
     * Gets the tournament history of the player.
     *
     * @return the player's tournament history.
     */
    public List<TournamentHistory> getTournamentHistory() {
        return tournamentHistory;
    }

    /**
     * Sets the tournament history of the player.
     *
     * @param tournamentHistory the tournament history to set.
     */
    public void setTournamentHistory(List<TournamentHistory> tournamentHistory) {
        this.tournamentHistory = tournamentHistory;
    }

    /**
     * Checks if the player's profile is public.
     *
     * @return true if the profile is public, false otherwise.
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets the player's profile visibility.
     *
     * @param isPublic true to make the profile public, false otherwise.
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    /**
     * Calculates and returns the player's age.
     *
     * @return the player's age.
     */
    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    /**
     * Returns a string representation of the profile.
     *
     * @return a string with the profile details.
     */
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
