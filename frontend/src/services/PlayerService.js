import { ProtectedPlayerAPI } from "./ProtectedPlayerAPI";
import { PlayerAPI } from "./PlayerAPI";

class PlayerService {

    async getProfile(playerId) {
        if (playerId) {
            return ProtectedPlayerAPI.get(`/profile/${playerId}`);
        } else {
            return ProtectedPlayerAPI.get("/profile"); // Endpoint to get the authenticated user
        }
    }

    async updateProfile(playerId, profile) {
        return ProtectedPlayerAPI.put(`/profile/${playerId}`, profile);
    }

    async getPlayerRatingHistory(playerId) {
        return ProtectedPlayerAPI.get(`/profile/rating-history/${playerId}`);
    }

    async getLeaderboard() {
        return PlayerAPI.get("/profile/leaderboard");
    }

    async reportPlayer(report){
        return ProtectedPlayerAPI.post("/player/reports", report);
    }

    async searchPlayers(query) {
        return ProtectedPlayerAPI.get(`/player/search?query=${query}`);
    }

    async followPlayer(followerId, followedId) {
        return ProtectedPlayerAPI.post(`/player/${followerId}/follow/${followedId}`);
    }

    async unfollowPlayer(followerId, followedId) {
        return ProtectedPlayerAPI.delete(`/player/${followerId}/unfollow/${followedId}`);
    }

    async getFollowers(playerId) {
        return ProtectedPlayerAPI.get(`/player/${playerId}/followers`);
    }

    async getFollowing(playerId) {
        return ProtectedPlayerAPI.get(`/player/${playerId}/following`);
    }
}

export default new PlayerService();