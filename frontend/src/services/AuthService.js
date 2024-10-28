import { AuthAPI } from "./AuthAPI";

class AuthService {
    async loginUser(credentials) {
      return await AuthAPI.post("/auth/login", credentials);
    }

    async registerUser(userData){
      return await AuthAPI.post("/auth/register", userData);
    }

    async requestPasswordReset(email){
        return await AuthAPI.post("/auth/request-password-reset", email);
    }

}

export default new AuthService();
