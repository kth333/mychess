import { TournamentAPI } from "./TournamentAPI";
import { ProtectedTournamentAPI } from "./ProtectedTournamentAPI";

class TournamentService {
    async createTournament(tournament) {
      return await ProtectedTournamentAPI.post("/tournaments/admin/create", tournament);
    }

    async updateTournament(tournament) {
      return await ProtectedTournamentAPI.put("/tournaments/admin/update", tournament);
    }

    async getAllTournaments(page) {
        console.log("/tournaments/public/all?page=" + page);
      return await TournamentAPI.get("/tournaments/public/all?page=" + page);
    }

    async getTournamentByName(tournamentName) {
      return await TournamentAPI.get(`/tournaments/public/get/${tournamentName}`);
    }

    async getTournamentById(tournamentId) {
      return await TournamentAPI.get(`/tournaments/public/get/id/${tournamentId}`);
    }

    async signUp(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/player/signup/${tournamentId}`);
    }

    async startTournament(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/start/${tournamentId}`);
    }

    async startNextRound(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/next-round/${tournamentId}`);
    }

    async completeTournament(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/complete/${tournamentId}`);
    }

    async leaveTournament(tournamentId) {
      return await ProtectedTournamentAPI.delete(`/tournaments/player/leave/${tournamentId}`);
    }

    async removePlayerFromTournament(tournamentId, playerId) {
      return await ProtectedTournamentAPI.delete(`/tournaments/admin/remove/${tournamentId}/${playerId}`);
    }

    

}

export default new TournamentService();