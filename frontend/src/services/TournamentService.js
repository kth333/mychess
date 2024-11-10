import { TournamentAPI } from "./TournamentAPI";
import { ProtectedTournamentAPI } from "./ProtectedTournamentAPI";

class TournamentService {
    async createTournament(tournament) {
      return await ProtectedTournamentAPI.post("/tournaments/admin", tournament);
    }

    async updateTournament(tournament) {
      return await ProtectedTournamentAPI.put("/tournaments/admin", tournament);
    }

    async getAllTournaments(page) {
        console.log("/tournaments/public/all?page=" + page);
      return await TournamentAPI.get("/tournaments/public/all?page=" + page);
    }

    async getTournamentByName(tournamentName) {
      return await TournamentAPI.get(`/tournaments/public/name/${tournamentName}`);
    }

    async getTournamentById(tournamentId) {
      return await TournamentAPI.get(`/tournaments/public/id/${tournamentId}`);
    }

    async signUp(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/player/${tournamentId}`);
    }

    async startTournament(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/${tournamentId}/status/in-progress`);
    }

    async startNextRound(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/${tournamentId}/next-round`);
    }

    async completeTournament(tournamentId) {
      return await ProtectedTournamentAPI.post(`/tournaments/admin/${tournamentId}/status/completed`);
    }

    async leaveTournament(tournamentId) {
      return await ProtectedTournamentAPI.delete(`/tournaments/player/${tournamentId}/leave`);
    }

    async removePlayerFromTournament(tournamentId, playerId) {
      return await ProtectedTournamentAPI.delete(`/tournaments/admin/${tournamentId}/players/${playerId}`);
    }

    

}

export default new TournamentService();