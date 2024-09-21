import { TournamentAPI } from "./TournamentAPI";
import { ProtectedTournamentAPI } from "./ProtectedTournamentAPI";

class TournamentService {
    async createTournament(tournament) {
      return await ProtectedTournamentAPI.post("/tournaments/admin/create", tournament);
    }

    async getAllTournaments() {
      return await TournamentAPI.get("/tournaments/public/all");
    }

    async getTournamentByName(tournamentName) {
      return await TournamentAPI.get(`/tournaments/public/get/${tournamentName}`);
    }
    

}

export default new TournamentService();