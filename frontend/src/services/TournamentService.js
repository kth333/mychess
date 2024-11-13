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
      return await TournamentAPI.get("/tournaments/all?page=" + page);
    }

    async getUpcomingTournaments(page) {
      return await TournamentAPI.get("/tournaments/upcoming?page=" + page);
    }

    async getTournamentByName(tournamentName) {
      return await TournamentAPI.get(`/tournaments/name/${tournamentName}`);
    }

    async getTournamentById(tournamentId) {
      return await TournamentAPI.get(`/tournaments/id/${tournamentId}`);
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
      return await ProtectedTournamentAPI.delete(`/tournaments/player/${tournamentId}`);
    }

    async removePlayerFromTournament(tournamentId, playerId) {
      return await ProtectedTournamentAPI.delete(`/tournaments/admin/${tournamentId}/players/${playerId}`);
    }

    async getPlayersByTournament(tournamentId) {
        return await TournamentAPI.get(`/tournaments/${tournamentId}/players`);
    }
}

export default new TournamentService();