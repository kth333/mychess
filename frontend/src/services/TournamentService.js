import { TournamentAPI } from "./TournamentAPI";
import { ProtectedTournamentAPI } from "./ProtectedTournamentAPI";

class TournamentService {
    async createTournament(tournament) {
      return await ProtectedTournamentAPI.post("/tournaments/admin/create", tournament);
    }

    async getAllTournaments() {
      return await TournamentAPI.get("/tournaments/public/all");
    }
    

}

export default new TournamentService();