import { MatchAPI } from "./MatchAPI";
import { ProtectedMatchAPI } from "./ProtectedMatchAPI";

class MatchService {
  
    async getAllMatchesById(tournamentId) {
        return await MatchAPI.get(`/matches/all/${tournamentId}`);
    }

    async getAllMatchesByIdAndRound(tournamentId, roundNumber) {
        return await MatchAPI.get(`/matches/${tournamentId}/round/${roundNumber}`);
    }

    async completeMatch(matchId, match) {
        return await ProtectedMatchAPI.post(`/matches/admin/${matchId}/status/completed/match`, match);
    }

    async getAllMatchResults(tournamentId) {
        return await MatchAPI.get(`/matches/all/result/${tournamentId}`);
    }

    async getTournamentResults(tournamentId) {
        return await MatchAPI.get(`/matches/tournament/${tournamentId}/results`);
    }

    async updateMatchTime(matchId){
        return await ProtectedMatchAPI.post(`/matches/admin/${matchId}/scheduled-time`);
    }

    async scheduleMatch(matchId, scheduledTime) {
        return await ProtectedMatchAPI.put(`/matches/admin/${matchId}/scheduled-time`, scheduledTime);
    }
  
}

export default new MatchService();