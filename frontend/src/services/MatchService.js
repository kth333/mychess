import { MatchAPI } from "./MatchAPI";
import { ProtectedMatchAPI } from "./ProtectedMatchAPI";

class MatchService {
  
    async getAllMatchesById(tournamentId) {
        return await MatchAPI.get(`/matches/public/all/${tournamentId}`);
    }

    async getAllMatchesByIdAndRound(tournamentId, roundNumber) {
        return await MatchAPI.get(`/matches/public/all/${tournamentId}/round/${roundNumber}`);
    }

    async completeMatch(matchId, match) {
        return await ProtectedMatchAPI.post(`/matches/admin/complete/${matchId}`, match);
    }


  
}

export default new MatchService();