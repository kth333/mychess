package com.g1.mychess.player.dto;

import java.time.LocalDate;

import com.g1.mychess.player.model.CustomChessRank;

public class PublicPlayerProfileDTO {

    private Long playerId;
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String gender;
    private String country;
    private String region;
    private String city;
    private LocalDate birthDate;
    private CustomChessRank rank;
    private int glickoRating;
    private int totalWins;
    private int totalLosses;
    private int totalDraws;
    private boolean isPublic;
    private int age;

    public PublicPlayerProfileDTO(Long playerId, String fullName, String bio, String avatarUrl, String gender, String country, String region, String city, LocalDate birthDate, CustomChessRank rank, int glickoRating, int totalWins, int totalLosses, int totalDraws, boolean isPublic, int age) {
        this.playerId = playerId;
        this.fullName = fullName;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.country = country;
        this.region = region;
        this.city = city;
        this.birthDate = birthDate;
        this.rank = rank;
        this.glickoRating = glickoRating;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.totalDraws = totalDraws;
        this.isPublic = isPublic;
        this.age = age;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

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
