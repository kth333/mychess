import { ProtectedPlayerAPI } from "./ProtectedPlayerAPI";

class PlayerService {
  
    async getProfile() {
        return ProtectedPlayerAPI.get("/player/profile");
    }

  
}

export default new PlayerService();