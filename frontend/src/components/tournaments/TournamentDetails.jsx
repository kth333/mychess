import React, { Component } from 'react';
import { Card } from '../ui/card';
import { Button } from '../ui/button';
import { useParams, Link } from 'react-router-dom';
import withNavigateandLocation from '../withNavigateandLocation';
import TournamentService from '../../services/TournamentService';
import MatchService from '../../services/MatchService';
import '../styles/tournamentResults.css';

class TournamentDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            showPlayers: false,
            tournament: null,
            matches: [],
            players: [],
            matchResults: [],
            selectedWinners: {},
            filteredMatches: [],
            searchRound: '',
            playerResults: [],
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
                await this.fetchMatchResults();
                await this.fetchTournamentResults();
            });
            console.log(tournamentRes.data);
        } catch (error) {
            console.error("Failed to fetch tournament", error);
        }
    };

    fetchTournamentResults = async () => {
        const { id } = this.state.tournament;
        try {
            // Replace with actual API call to fetch tournament results
            const resultsRes = await MatchService.getTournamentResults(id);
            this.setState({ playerResults: resultsRes.data.playerResults });
        } catch (error) {
            console.error("Failed to fetch tournament results", error);
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

    fetchMatchResults = async () => {
        const { id } = this.state.tournament;
        try {
            const res = await MatchService.getAllMatchResults(id);
            this.setState({ matchResults: res.data });
            console.log(res.data);
        } catch (error) {
            console.error("Failed to fetch match results", error);
        }
    };


    completeMatch = async (matchId) => {
        const { selectedWinners } = this.state;
        const { winnerId, loserId, isDraw } = selectedWinners[matchId];
        try {
            await MatchService.completeMatch(matchId, { winnerId, loserId, isDraw });
            await this.fetchMatches();
            await this.fetchMatchResults();
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
            alert("Next round started successfully!");
            this.fetchMatches();
        } catch (error) {
            alert("Failed to start next round\nReason: " + error.response.data);
        }
    };

    completeTournament = async () => {
        try {
            await TournamentService.completeTournament(this.state.tournament.id);
            alert("Tournament completed successfully!");
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

        if (!players.length) return <p className="text-center text-primary">No players signed up for this tournament.</p>;

        return (
            <div className="grid grid-cols-1 gap-4 mt-4">
                {players.map((player) => (
                    <div key={player.id} className="flex items-center p-4 bg-primary rounded-lg shadow">
                        {/* Display chess rank title next to the username */}
                        <div className="flex-grow">
                            <p className="font-semibold">{player.username}</p>
                            <p className="text-primary text-sm">
                                Rank: {this.getChessRankTitle(player.glickoRating || 0)}
                            </p>
                            <p className="text-primary text-sm">
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
        const { filteredMatches, matchResults } = this.state;
        if (!filteredMatches.length) return <p>No matches available yet.</p>;
    
        return (
            <table className="table-auto w-full border-collapse border border-primary my-4 shadow-md">
                <thead className="bg-primary">
                    <tr>
                        <th className="border-primary px-4 py-2">Match ID</th>
                        <th className="border-primary px-4 py-2">Player 1</th>
                        <th className="border-primary px-4 py-2">Player 2</th>
                        <th className="border-primary px-4 py-2">Round</th>
                        <th className="border-primary px-4 py-2">Status</th>
                        <th className="border-primary px-4 py-2">Action</th>
                        <th className="border-primary px-4 py-2">Result</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredMatches.map((match) => {
                        // Find the result for the current match
                        const result = matchResults.find((r) => r.matchId === match.id);
    
                        return (
                            <tr key={match.id} className="bg-primary">
                                <td className="border-primary px-4 py-2">{match.id}</td>
                                <td className="border-primary px-4 py-2">{match.participantIds[0]}</td>
                                <td className="border-primary px-4 py-2">{match.participantIds[1]}</td>
                                <td className="border-primary px-4 py-2">{match.roundNumber}</td>
                                <td className="border-primary px-4 py-2">{match.status}</td>
                                <td className="border-primary px-4 py-2 flex flex-col items-center space-y-2">
                                    {match.status !== 'COMPLETED' && (
                                        <select onChange={(e) => this.handleWinnerChange(match.id, e.target.value)} className="border-primary rounded p-1">
                                            <option value="">Select Winner</option>
                                            <option value="player1">Player 1 Wins</option>
                                            <option value="player2">Player 2 Wins</option>
                                            <option value="draw">Draw</option>
                                        </select>
                                    )}
                                    <Button className="rounded-lg btn btn-success mt-2" onClick={() => this.completeMatch(match.id)} disabled={match.status === 'COMPLETED'}>
                                        Complete Match
                                    </Button>
                                </td>

                                <td className="border-primary px-4 py-2">
                                    {result ? (
                                        result.isDraw ? (
                                            "Draw"
                                        ) : (
                                            <>
                                                Winner: {result.winnerId} <br />
                                                Loser: {result.loserId}
                                            </>
                                        )
                                    ) : (
                                        "Pending"
                                    )}
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
        );
    };

    renderTournamentResults = () => {
        const { playerResults } = this.state;
        if (!playerResults.length) return <p className="text-center">No results available yet.</p>;
    
        // Sort playerResults by points in descending order
        const sortedResults = [...playerResults].sort((a, b) => b.points - a.points);
    
        // Helper function to determine the row style based on rank
        const getRowStyle = (index) => {
            switch (index) {
                case 0: return 'bg-gold';  // First place: Light gold
                case 1: return 'bg-silver'; // Second place: Light silver
                case 2: return 'bg-bronze'; // Third place: Light bronze
                default: return '';
            }
        };
    
        return (
            <table className="table w-full mt-4 border border-primary">
                <thead className="bg-primary">
                    <tr>
                        <th className="px-4 py-2 border-primary">Rank</th>
                        <th className="px-4 py-2 border-primary">Player ID</th>
                        <th className="px-4 py-2 border-primary">Points</th>
                    </tr>
                </thead>
                <tbody>
                    {sortedResults.map((result, index) => (
                        <tr key={index} className={getRowStyle(index)}>
                            <td className="px-4 py-2 border-primary">{index + 1}</td>
                            <td className="px-4 py-2 border-primary">{result.playerId}</td>
                            <td className="px-4 py-2 border-primary">{result.points}</td>
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
                <Card className="p-6 bg-primary rounded-lg shadow-lg w-full">
                    <h2 className="text-3xl font-bold text-center text-primary mb-4">{name}</h2>
                    <p className="text-center text-primary mb-6">{description}</p>
                    <p className="text-center text-primary mb-8">Host: Admin{adminId}</p>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-6">
                        <div>
                            <h5 className="text-xl font-semibold text-primary mb-4">Tournament Details</h5>
                            <p className="text-primary">
                                <strong>Start:</strong> {new Date(startDateTime).toLocaleString()}</p>
                            <p className="text-primary"><strong>End:</strong> {new Date(endDateTime).toLocaleString()}
                            </p>
                            <p className="text-primary"><strong>Format:</strong> {format}</p>
                            <p className="text-primary"><strong>Status:</strong> {status}</p>
                            <p className="text-primary"><strong>Time
                                Control:</strong> {timeControlSetting.baseTimeMinutes}+{timeControlSetting.incrementSeconds}
                            </p>
                        </div>
                        <div>
                            <h5 className="text-xl font-semibold text-primary mb-4">Requirements</h5>
                            <p className="text-primary"><strong>Min Rating:</strong> {minRating}</p>
                            <p className="text-primary"><strong>Max Rating:</strong> {maxRating}</p>
                            <p className="text-primary"><strong>Affects Rating:</strong> {affectsRating ? 'Yes' : 'No'}
                            </p>
                            <p className="text-primary"><strong>Min Age:</strong> {minAge}</p>
                            <p className="text-primary"><strong>Max Age:</strong> {maxAge}</p>
                            <p className="text-primary"><strong>Required Gender:</strong> {requiredGender || "Any"}</p>
                            <p className="text-primary"><strong>Location:</strong> {city}, {region}, {country}</p>
                        </div>
                    </div>

                    <div className="mb-6">
                        <p className="text-primary text-sm"><strong>Registration:</strong> {new Date(registrationStartDate).toLocaleString()} to {new Date(registrationEndDate).toLocaleString()}</p>
                    </div>

                    {/* Toggle Button for Player List */}
                    <div className="mb-6">
                        <h5 className="text-xl font-semibold text-primary">Players</h5>
                        <button
                            onClick={this.togglePlayersList}
                            className="btn btn-primary mb-4">
                            {showPlayers ? 'Hide Players' : 'Show Players'}
                        </button>
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
                                <Link className="btn btn-primary"
                                      to={`/schedule-matches/${tournament.id}`}>Schedule Matches</Link>      
                                <button className="btn btn-primary" onClick={this.startTournament}>Start
                                    Tournament</button>
                                <button className="btn btn-primary" onClick={this.startNextRound}>Next Round</button>
                                    Tournament</button>
                            </>
                        )}
                    </div>
                    {/* Results section */}
                    <div className="mt-8">
                        <h3 className="text-2xl font-semibold text-primary mb-4 text-center">Results</h3>
                        {this.renderTournamentResults()}
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