import { ProtectedPlayerAPI } from "./ProtectedPlayerAPI";

class PlayerService {
  
    async getProfile() {
        return ProtectedPlayerAPI.get("/profile/");
    }

    async updateProfile(playerId, profile) {
        return ProtectedPlayerAPI.put(`/profile/${playerId}`, profile);
    }


}

export default new PlayerService();