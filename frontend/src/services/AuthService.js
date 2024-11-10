import { AuthAPI } from "./AuthAPI";

class AuthService {
    async loginUser(credentials) {
      return await AuthAPI.post("/auth/session/new", credentials);
    }

    async registerUser(userData){
      return await AuthAPI.post("/auth/registration", userData);
    }

    async requestPasswordReset(email){
        console.log("Sending reset password request for:", email);
        return await AuthAPI.post(`/auth/password-recovery/${email}`);
    }

    async resetPassword(newPasswordAndToken){
        return await AuthAPI.post("/auth/password-recovery", newPasswordAndToken);
    }

}

export default new AuthService();
