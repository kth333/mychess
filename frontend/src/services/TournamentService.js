import { TournamentAPI } from "./TournamentAPI";
import { ProtectedTournamentAPI } from "./ProtectedTournamentAPI";

class TournamentService {
    async createTournament(tournament) {
      return await ProtectedTournamentAPI.post("/tournaments/admin/create", tournament);
    }

    async updateTournament(tournament) {
      return await ProtectedTournamentAPI.put("/tournaments/admin/update", tournament);
    }

    async getAllTournaments() {
      return await TournamentAPI.get("/tournaments/public/all");
    }

    async getTournamentByName(tournamentName) {
      return await TournamentAPI.get(`/tournaments/public/get/${tournamentName}`);
    }

    async signUp(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/player/signup/${tournamentId}`);
    }

    async startTournament(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/start/${tournamentId}`);
    }

    async prepareNextRound(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/next-round/${tournamentId}`);
    }
    

}

export default new TournamentService();