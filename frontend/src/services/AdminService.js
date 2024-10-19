import { ProtectedAdminAPI } from "./ProtectedAdminAPI";

class AdminService {
    async blacklistPlayer(data) {
      return await ProtectedAdminAPI.post("/admin/blacklist", data);
    }

    async whitelistPlayer(data){
      return await ProtectedAdminAPI.post("/admin/whitelist", data);
    }

}

export default new AdminService();
