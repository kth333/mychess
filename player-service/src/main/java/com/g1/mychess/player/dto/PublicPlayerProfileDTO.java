package com.g1.mychess.player.dto;

import java.time.LocalDate;
import com.g1.mychess.player.model.CustomChessRank;

public class PublicPlayerProfileDTO extends PlayerProfileDTO {

    private boolean isPublic;

    // Constructor
    public PublicPlayerProfileDTO(Long playerId, String fullName, String bio, String avatarUrl, String gender, 
                                  String country, String region, String city, LocalDate birthDate, CustomChessRank rank, 
                                  int glickoRating, double ratingDeviation, double volatility, int totalWins, 
                                  int totalLosses, int totalDraws, int age, boolean isPublic) {
        super(playerId, fullName, bio, avatarUrl, gender, country, region, city, birthDate, rank, glickoRating, 
              ratingDeviation, volatility, totalWins, totalLosses, totalDraws, age);
        this.isPublic = isPublic;
    }

    // Getter and Setter for isPublic
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}









/* package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.g1.mychess.player.model.CustomChessRank;

public class PublicPlayerProfileDTO {

    private Long playerId;
    private String username;
    private String bio;
    private String avatarUrl;
    private CustomChessRank rank;
    private int glickoRating;
    private int totalWins;
    private int totalLosses;
    private int totalDraws;


    public PublicPlayerProfileDTO(Long playerId, String username, String bio, String avatarUrl, CustomChessRank rank, int glickoRating, int totalWins, int totalLosses, int totalDraws) {
        this.playerId = playerId;
        this.username = username;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.rank = rank;
        this.glickoRating = glickoRating;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalDraws = totalDraws;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    // public String getFullName() {
    //     return fullName;
    // }

    // public void setFullName(String fullName) {
    //     this.fullName = fullName;
    // }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public CustomChessRank getRank() {
        return rank;
    }

    public void setRank(CustomChessRank rank) {
        this.rank = rank;
    }

    public int getGlickoRating() {
        return glickoRating;
    }

    public void setGlickoRating(int glickoRating) {
        this.glickoRating = glickoRating;
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
}
*/