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

    async resetPassword(newPasswordAndToken){
        return await AuthAPI.post("/auth/reset-password", newPasswordAndToken);
    }

}

export default new AuthService();
