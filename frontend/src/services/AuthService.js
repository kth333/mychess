import { API } from "./API";

class AuthService {
    async loginUser(credentials) {
      return await API.post("/auth/login", credentials);
    }

    async registerUser(userData){
      return await API.post("/auth/register", userData);
    }

}

export default new AuthService();
