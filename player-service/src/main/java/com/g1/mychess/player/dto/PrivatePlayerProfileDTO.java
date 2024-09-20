package com.g1.mychess.player.dto;

import java.time.LocalDate;
import com.g1.mychess.player.model.CustomChessRank;

public class PrivatePlayerProfileDTO extends PlayerProfileDTO {

    private String email;
    private String username;

    public PrivatePlayerProfileDTO(Long playerId, String fullName, String bio, String avatarUrl, String gender, 
                                   String country, String region, String city, LocalDate birthDate, CustomChessRank rank, 
                                   int glickoRating, double ratingDeviation, double volatility, int totalWins, 
                                   int totalLosses, int totalDraws, int age, String email, String username) {
        super(playerId, fullName, bio, avatarUrl, gender, country, region, city, birthDate, rank, glickoRating, 
              ratingDeviation, volatility, totalWins, totalLosses, totalDraws, age);
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}






/*package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.g1.mychess.player.model.CustomChessRank;

public class PrivatePlayerProfileDTO {

    private Long playerId;
    private String username;
    private String email;

    public PrivatePlayerProfileDTO(Long playerId, String username, String email) {
        this.playerId = playerId;
        this.username = username;
        this.email = email;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
}
*/