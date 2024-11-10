import { ProtectedAdminAPI } from "./ProtectedAdminAPI";

class AdminService {
    async blacklistPlayer(data) {
      return await ProtectedAdminAPI.post("/admin/blacklists", data);
    }

    async whitelistPlayer(data){
      return await ProtectedAdminAPI.post("/admin/whitelists", data);
    }

}

export default new AdminService();
