import { ProtectedPlayerAPI } from "./ProtectedPlayerAPI";

class PlayerService {
  
    async getProfile() {
        return ProtectedPlayerAPI.get("/profile/");
    }


}

export default new PlayerService();