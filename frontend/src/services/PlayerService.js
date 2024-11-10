import { ProtectedPlayerAPI } from "./ProtectedPlayerAPI";

class PlayerService {
  
    async getProfile() {
        return ProtectedPlayerAPI.get("/profile/");
    }

    async updateProfile(playerId, profile) {
        return ProtectedPlayerAPI.put(`/profile/${playerId}`, profile);
    }

    async getPlayerRatingHistory(playerId) {
        return ProtectedPlayerAPI.get(`/profile/rating-history/${playerId}`);
    }


}

export default new PlayerService();