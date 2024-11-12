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

}

export default new PlayerService();