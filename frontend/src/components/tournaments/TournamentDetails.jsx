import React, { Component } from 'react';
import { Card } from '../ui/card';
import { Button } from '../ui/button';
import { useParams, Link } from 'react-router-dom';
import withNavigateandLocation from '../withNavigateandLocation';
import TournamentService from '../../services/TournamentService';
import MatchService from '../../services/MatchService';

class TournamentDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showPlayers: false,
            tournament: null,
            matches: [],
            players: [],
            selectedWinners: {},
            filteredMatches: [],
            searchRound: '',
        };
    }

    async componentDidMount() {
        await this.fetchData();
    }

    fetchData = async () => {
        const { name } = this.props.params;
        try {
            const tournamentRes = await TournamentService.getTournamentByName(name);
            this.setState({ tournament: tournamentRes.data }, async () => {
                await this.fetchMatches();
                await this.fetchPlayers();
            });
        } catch (error) {
            console.error("Failed to fetch tournament", error);
        }
    };

    fetchPlayers = async () => {
        const { id } = this.state.tournament;
        try {
            const res = await TournamentService.getPlayersByTournament(id);
            this.setState({ players: res.data });
        } catch (error) {
            console.error("Failed to fetch players", error);
        }
    };

    fetchMatches = async () => {
        const { id } = this.state.tournament;
        try {
            const res = await MatchService.getAllMatchesById(id);
            this.setState({ matches: res.data, filteredMatches: res.data });
        } catch (error) {
            console.error("Failed to fetch matches", error);
        }
    };

    completeMatch = async (matchId) => {
        const { selectedWinners } = this.state;
        const { winnerId, loserId, isDraw } = selectedWinners[matchId];
        try {
            await MatchService.completeMatch(matchId, { winnerId, loserId, isDraw });
            this.fetchMatches();
        } catch (error) {
            alert("Failed to complete match\nReason: " + error.response.data);
        }
    };

    handleWinnerChange = (matchId, value) => {
        const selectedWinners = { ...this.state.selectedWinners };
        const match = this.state.matches.find(m => m.id === matchId);
        const player1 = match.participantIds[0];
        const player2 = match.participantIds[1];

        selectedWinners[matchId] = value === 'draw'
            ? { winnerId: null, loserId: null, isDraw: true }
            : { winnerId: value === 'player1' ? player1 : player2, loserId: value === 'player1' ? player2 : player1, isDraw: false };

        this.setState({ selectedWinners });
    };

    signUp = async () => {
        try {
            await TournamentService.signUp(this.state.tournament.id);
            await this.fetchPlayers();
        } catch (error) {
            alert("Failed to sign up for tournament\nReason: " + error.response.data);
        }
    };

    leaveTournament = async () => {
        try {
            await TournamentService.leaveTournament(this.state.tournament.id);
            await this.fetchPlayers();
        } catch (error) {
            alert("Failed to leave tournament\nReason: " + error.response.data);
        }
    };

    startTournament = async () => {
        try {
            await TournamentService.startTournament(this.state.tournament.id);
            this.fetchMatches();
        } catch (error) {
            alert("Failed to start tournament\nReason: " + error.response.data);
        }
    };

    startNextRound = async () => {
        try {
            await TournamentService.startNextRound(this.state.tournament.id);
            this.fetchMatches();
        } catch (error) {
            alert("Failed to start next round\nReason: " + error.response.data);
        }
    };

    completeTournament = async () => {
        try {
            await TournamentService.completeTournament(this.state.tournament.id);
            this.fetchData();
        } catch (error) {
            alert("Failed to complete tournament\nReason: " + error.response.data);
        }
    };

    removePlayerFromTournament = async (playerId) => {
        try {
            await TournamentService.removePlayerFromTournament(this.state.tournament.id, playerId);
            this.fetchPlayers();
            this.fetchMatches();
        } catch (error) {
            alert("Failed to remove player\nReason: " + error.response.data);
        }
    };

    handleSearchChange = (e) => {
        const searchRound = e.target.value;
        this.setState({ searchRound }, this.filterMatches);
    };

    filterMatches = () => {
        const { matches, searchRound } = this.state;
        const filteredMatches = matches.filter((match) => match.roundNumber.toString().includes(searchRound));
        this.setState({ filteredMatches });
    };

    togglePlayersList = () => {
        this.setState((prevState) => ({ showPlayers: !prevState.showPlayers }));
    };

    getChessRankTitle = (glickoRating) => {
        if (glickoRating < 1200) {
            return "Novice";
        } else if (glickoRating < 1600) {
            return "Intermediate";
        } else if (glickoRating < 2000) {
            return "Advanced";
        } else if (glickoRating < 2400) {
            return "Expert";
        } else if (glickoRating < 2700) {
            return "Master";
        } else {
            return "Grandmaster";
        }
    };

    renderPlayersList = () => {
        const { players } = this.state;
        const isAdmin = sessionStorage.getItem("role") === 'ROLE_ADMIN';

        if (!players.length) return <p className="text-center text-gray-500">No players signed up for this tournament.</p>;

        return (
            <div className="grid grid-cols-1 gap-4 mt-4">
                {players.map((player) => (
                    <div key={player.id} className="flex items-center p-4 bg-white rounded-lg shadow">
                        {/* Display chess rank title next to the username */}
                        <div className="flex-grow">
                            <p className="font-semibold">{player.username}</p>
                            <p className="text-gray-600 text-sm">
                                Rank: {this.getChessRankTitle(player.glickoRating || 0)}
                            </p>
                            <p className="text-gray-600 text-sm">
                                Rating: {player.glickoRating || "Unrated"}
                            </p>
                        </div>
                        <Link to={`/profile/${player.id}`} className="btn btn-secondary mx-2">View Profile</Link>
                        {isAdmin && (
                            <Button className="btn btn-danger" onClick={() => this.removePlayerFromTournament(player.id)}>Remove</Button>
                        )}
                    </div>
                ))}
            </div>
        );
    };

    renderMatchesTable = () => {
        const { filteredMatches } = this.state;
        if (!filteredMatches.length) return <p>No matches available yet.</p>;

        return (
            <table className="table-auto w-full border-collapse border border-gray-300 my-4 shadow-md">
                <thead className="bg-gray-200">
                <tr>
                    <th className="border px-4 py-2">Match ID</th>
                    <th className="border px-4 py-2">Player 1</th>
                    <th className="border px-4 py-2">Player 2</th>
                    <th className="border px-4 py-2">Round</th>
                    <th className="border px-4 py-2">Status</th>
                    <th className="border px-4 py-2">Action</th>
                </tr>
                </thead>
                <tbody>
                {filteredMatches.map((match) => (
                    <tr key={match.id} className="bg-white">
                        <td className="border px-4 py-2">{match.id}</td>
                        <td className="border px-4 py-2">{match.participantIds[0]}</td>
                        <td className="border px-4 py-2">{match.participantIds[1]}</td>
                        <td className="border px-4 py-2">{match.roundNumber}</td>
                        <td className="border px-4 py-2">{match.status}</td>
                        <td className="border px-4 py-2 flex items-center space-x-2">
                            {match.status !== 'COMPLETED' && (
                                <select onChange={(e) => this.handleWinnerChange(match.id, e.target.value)} className="border rounded p-1 mr-2">
                                    <option value="">Select Winner</option>
                                    <option value="player1">Player 1 Wins</option>
                                    <option value="player2">Player 2 Wins</option>
                                    <option value="draw">Draw</option>
                                </select>
                            )}
                            <Button className="btn btn-success" onClick={() => this.completeMatch(match.id)} disabled={match.status === 'COMPLETED'}>Complete Match</Button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    render() {
        const { tournament, searchRound, showPlayers } = this.state;
        const isPlayer = sessionStorage.getItem("role") === 'ROLE_PLAYER';

        if (!tournament) return <p>Loading...</p>;

        const { name, description, adminId, startDateTime, endDateTime, registrationStartDate, registrationEndDate, format, status, minRating, maxRating, affectsRating, minAge, maxAge, requiredGender, city, region, country, timeControlSetting } = tournament;

        return (
            <div className="p-6 max-w-4xl mx-auto">
                <Card className="p-6 bg-white rounded-lg shadow-lg">
                    <h2 className="text-3xl font-bold text-center text-gray-800 mb-4">{name}</h2>
                    <p className="text-center text-gray-700 mb-6">{description}</p>
                    <p className="text-center text-gray-500 mb-8">Host: Admin{adminId}</p>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-6">
                        <div>
                            <h5 className="text-xl font-semibold text-gray-800 mb-4">Tournament Details</h5>
                            <p className="text-gray-700">
                                <strong>Start:</strong> {new Date(startDateTime).toLocaleString()}</p>
                            <p className="text-gray-700"><strong>End:</strong> {new Date(endDateTime).toLocaleString()}
                            </p>
                            <p className="text-gray-700"><strong>Format:</strong> {format}</p>
                            <p className="text-gray-700"><strong>Status:</strong> {status}</p>
                            <p className="text-gray-700"><strong>Time
                                Control:</strong> {timeControlSetting.baseTimeMinutes}+{timeControlSetting.incrementSeconds}
                            </p>
                        </div>
                        <div>
                            <h5 className="text-xl font-semibold text-gray-800 mb-4">Requirements</h5>
                            <p className="text-gray-700"><strong>Min Rating:</strong> {minRating}</p>
                            <p className="text-gray-700"><strong>Max Rating:</strong> {maxRating}</p>
                            <p className="text-gray-700"><strong>Affects Rating:</strong> {affectsRating ? 'Yes' : 'No'}
                            </p>
                            <p className="text-gray-700"><strong>Min Age:</strong> {minAge}</p>
                            <p className="text-gray-700"><strong>Max Age:</strong> {maxAge}</p>
                            <p className="text-gray-700"><strong>Required Gender:</strong> {requiredGender || "Any"}</p>
                            <p className="text-gray-700"><strong>Location:</strong> {city}, {region}, {country}</p>
                        </div>
                    </div>

                    <div className="mb-6">
                        <p className="text-gray-500 text-sm"><strong>Registration:</strong> {new Date(registrationStartDate).toLocaleString()} to {new Date(registrationEndDate).toLocaleString()}</p>
                    </div>

                    {/* Toggle Button for Player List */}
                    <div className="mb-6">
                        <h5 className="text-xl font-semibold text-gray-800">Players</h5>
                        <Button
                            onClick={this.togglePlayersList}
                            className="btn btn-secondary mb-4">
                            {showPlayers ? 'Hide Players' : 'Show Players'}
                        </Button>
                        {showPlayers && this.renderPlayersList()}
                    </div>

                    <input
                        type="text"
                        placeholder="Enter round number"
                        value={searchRound}
                        onChange={this.handleSearchChange}
                        className="input input-bordered input-accent mb-4 w-full"
                    />

                    {!isPlayer && this.renderMatchesTable()}

                    <div className="flex space-x-4 mt-6">
                        {isPlayer ? (
                            <>
                                <Button className="btn btn-primary" onClick={this.signUp}>Sign Up</Button>
                                <Button className="btn btn-danger" onClick={this.leaveTournament}>Leave</Button>
                            </>
                        ) : (
                            <>
                                <Link className="btn btn-primary"
                                      to={`/update-tournament/${tournament.name}`}>Update</Link>
                                <Button className="btn btn-primary" onClick={this.startTournament}>Start
                                    Tournament</Button>
                                <Button className="btn btn-primary" onClick={this.startNextRound}>Next Round</Button>
                                <Button className="btn btn-danger" onClick={this.completeTournament}>Complete
                                    Tournament</Button>
                            </>
                        )}
                    </div>
                </Card>
            </div>
        );
    }
}

const TournamentDetailsWithParams = (props) => (
    <TournamentDetails {...props} params={useParams()}/>
);

export default withNavigateandLocation(TournamentDetailsWithParams);