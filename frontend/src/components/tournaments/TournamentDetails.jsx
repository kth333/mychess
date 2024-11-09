import React, { Component } from 'react';
import { Card } from '../ui/card';
import { Button } from '../ui/button';
import { useParams, Link } from 'react-router-dom';
import withNavigateandLocation from '../withNavigateandLocation';
import TournamentService from '../../services/TournamentService';
import MatchService from '../../services/MatchService'; // Import MatchService

class TournamentDetails extends Component {
    constructor(props) {
        super(props);
        this.state = {
            tournament: null,
            matches: [], // Store the matches here
            selectedWinners: {}, // Store selected winners for each match
            filteredMatches: [], // Store filtered matches
            searchRound: '', // Store the search round number
        };
    }

    async componentDidMount() {
        await this.fetchData();
    }

    // Fetch tournament details
    fetchData = async () => {
        const { name } = this.props.params;
        try {
            const res = await TournamentService.getTournamentByName(name);
            console.log("Tournament data:", res.data);
            this.setState({ tournament: res.data }, this.fetchMatches);
        } catch (error) {
            console.error("Failed to fetch tournament", error);
        }
    };

    // Fetch matches associated with the tournament
    fetchMatches = async () => {
        const { id } = this.state.tournament;
        try {
            const res = await MatchService.getAllMatchesById(id);
            console.log("Matches data:", res.data);
            this.setState({ matches: res.data, filteredMatches: res.data });
        } catch (error) {
            console.error("Failed to fetch matches", error);
        }
    };

    // Complete match by ID
    completeMatch = async (matchId) => {
        const { selectedWinners } = this.state;
        const { winnerId, loserId, isDraw } = selectedWinners[matchId];

        let match = {
            winnerId: winnerId,
            loserId: loserId,
            isDraw: isDraw,
        };

        console.log("Completing match with data:", JSON.stringify(match));
        try {
            await MatchService.completeMatch(matchId, match).then(() => {this.fetchMatches();});
            console.log("Match completed successfully");
        } catch (error) {
            console.error("Failed to complete match", error);
            alert("Failed to complete match\nReason: " + error.response.data);
        }
    };

    // Handle winner selection
    handleWinnerChange = (matchId, value) => {
        const selectedWinners = { ...this.state.selectedWinners };
        if (value === 'draw') {
            selectedWinners[matchId] = { winnerId: null, loserId: null, isDraw: true };
        } else {
            const winnerId = value === 'player1' ? this.state.matches.find(m => m.id === matchId).participantIds[0] : this.state.matches.find(m => m.id === matchId).participantIds[1];
            const loserId = value === 'player1' ? this.state.matches.find(m => m.id === matchId).participantIds[1] : this.state.matches.find(m => m.id === matchId).participantIds[0];
            selectedWinners[matchId] = { winnerId, loserId, isDraw: false };
        }
        this.setState({ selectedWinners });
    };

    signUp = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.signUp(id);
            console.log("Register success");
        } catch (error) {
            console.error("Failed to sign up for tournament", error);
            alert("Failed to sign up for tournament\nReason: " + error.response.data);
        }
    };

    leaveTournament = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.leaveTournament(id);
            console.log("Leave success");
        } catch (error) {
            console.error("Failed to leave tournament", error);
            alert("Failed to leave tournament\nReason: " + error.response.data);
        }
    };

    startTournament = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.startTournament(id).then(() => {this.fetchMatches();});
            console.log("Start success");
        } catch (error) {
            console.error("Failed to start tournament", error);
            alert("Failed to start tournament\nReason: " + error.response.data);
        }
    };

    startNextRound = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.startNextRound(id).then(() => {this.fetchMatches();});
            console.log("Next round success");
        } catch (error) {
            console.error("Failed to start next round", error);
            alert("Failed to start next round\nReason: " + error.response.data);
        }
    };

    completeTournament = async () => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.completeTournament(id).then(() => {
                alert("Tournament completed successfully");
                this.fetchData(); // Refresh tournament data after completion
            });
            console.log("Complete tournament success");
        } catch (error) {
            console.error("Failed to complete tournament", error);
            alert("Failed to complete tournament\nReason: " + error.response.data);
        }
    };

    // Remove player from tournament
    removePlayerFromTournament = async (playerId) => {
        const { id } = this.state.tournament;
        try {
            await TournamentService.removePlayerFromTournament(id, playerId);
            console.log(`Player ${playerId} removed successfully`);
            this.fetchMatches(); // Refresh matches after removing a player
        } catch (error) {
            console.error(`Failed to remove player ${playerId} from tournament`, error);
            alert("Failed to remove player from tournament\nReason: " + error.response.data);
        }
    };

    // Handle search input change
    handleSearchChange = (event) => {
        const searchRound = event.target.value;
        this.setState({ searchRound }, this.filterMatches);
    };

    
    

    // Filter matches based on round number
    filterMatches = () => {
        const { matches, searchRound } = this.state;
        const filteredMatches = matches.filter((match) => {
            return match.roundNumber.toString().includes(searchRound);
        });
        this.setState({ filteredMatches });
    };

    renderMatchesTable = () => {
        const { filteredMatches } = this.state;

        if (filteredMatches.length === 0) {
            return <p>No matches available yet.</p>;
        }

        return (
            <table className="table-auto w-full border-collapse border border-gray-300 my-4">
                <thead>
                    <tr>
                        <th className="border px-4 py-2">Match ID</th>
                        <th className="border px-4 py-2">Player 1</th>
                        <th className="border px-4 py-2">Player 2</th>
                        <th className="border px-4 py-2">Round</th>
                        <th className="border px-4 py-2">Status</th>
                        <th className="border px-4 py-2">Action</th> {/* New column for actions */}
                    </tr>
                </thead>
                <tbody>
                    {filteredMatches.map((match) => (
                        <tr key={match.id}>
                            <td className="border px-4 py-2">{match.id}</td>
                            <td className="border px-4 py-2">{match.participantIds[0]}
                                <Button 
                                    className="btn btn-success mt-2" 
                                    onClick={() => this.removePlayerFromTournament(match.participantIds[0])}>
                                    Remove
                                </Button>   
                            </td>
                            <td className="border px-4 py-2">{match.participantIds[1]}
                                <Button 
                                    className="btn btn-success mt-2" 
                                    onClick={() => this.removePlayerFromTournament(match.participantIds[1])}>
                                    Remove
                                </Button>
                            </td>
                            <td className="border px-4 py-2">{match.roundNumber}</td>
                            <td className="border px-4 py-2">{match.status}</td>
                            <td className="border px-4 py-2">
                                {match.status !== 'COMPLETED' && <select
                                    onChange={(e) => this.handleWinnerChange(match.id, e.target.value)}
                                    className="border rounded p-1"
                                >
                                    <option value="">Select Winner</option>
                                    <option value="player1">Player 1 Wins</option>
                                    <option value="player2">Player 2 Wins</option>
                                    <option value="draw">Draw</option>
                                </select>}
                                
                                <Button 
                                    className="btn btn-success mt-2" 
                                    onClick={() => this.completeMatch(match.id)} 
                                    disabled={match.status === 'COMPLETED'} // Disable if not ongoing
                                >
                                    Complete Match
                                </Button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };

    render() {
        const { tournament, searchRound } = this.state;
        const userRole = sessionStorage.getItem("role");
        const isPlayer = userRole === 'ROLE_PLAYER';

        if (!tournament) {
            return <p>Loading...</p>;
        }

        const {
            adminId,
            name,
            description,
            startDateTime,
            endDateTime,
            registrationStartDate,
            registrationEndDate,
            format,
            status,
            minRating,
            maxRating,
            affectsRating,
            minAge,
            maxAge,
            requiredGender,
            city,
            region,
            country,
            timeControlSetting,
        } = tournament;

        return (
            <div className="p-6 max-w-4xl mx-auto">
                <Card className="p-4 bg-primary">
                    <h2 className="text-2xl font-bold text-primary">{name}</h2>
                    <p className="my-2 text-accent">{description}</p>
                    <p className="my-2 text-secondary">Host: admin{adminId}</p>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4 my-4">
                        <div>
                            <h5 className="text-xl font-semibold text-primary">Tournament Details</h5>
                            <ul className="list-disc ml-6">
                                <li>Start: {new Date(startDateTime).toLocaleString()}</li>
                                <li>End: {new Date(endDateTime).toLocaleString()}</li>
                                <li>Format: {format}</li>
                                <li>Status: {status}</li>
                            </ul>
                        </div>

                        <div>
                            <h5 className="text-xl font-semibold text-primary">Requirements</h5>
                            <ul className="list-disc ml-6">
                                <li>Min Rating: {minRating}</li>
                                <li>Max Rating: {maxRating}</li>
                                <li>Affects Rating: {affectsRating ? 'Yes' : 'No'}</li>
                                <li>Min Age: {minAge}</li>
                                <li>Max Age: {maxAge}</li>
                                <li>Required Gender: {requiredGender}</li>
                                <li>Location: {city}, {region}, {country}</li>
                            </ul>
                        </div>
                    </div>

                    <div className="my-4">
                        <h5 className="text-xl font-semibold text-primary">Time Control</h5>
                        <p className="text-secondary">
                            Base Time: {timeControlSetting.baseTimeMinutes} minutes, Increment: {timeControlSetting.incrementSeconds} seconds, Type: {timeControlSetting.timeControlType}
                        </p>
                        <p className="text-secondary">
                            Registration from {new Date(registrationStartDate).toLocaleString()} to {new Date(registrationEndDate).toLocaleString()}
                        </p>
                    </div>

                    <input
                        type="text"
                        placeholder="Enter round number"
                        value={searchRound}
                        onChange={this.handleSearchChange}
                        className="input input-bordered input-accent mb-4 w-full"
                    />

                    {!isPlayer && this.renderMatchesTable()}

                    {isPlayer ? (
                        <div>
                        <Button className="btn btn-primary mt-6" onClick={this.signUp}>
                            Sign Up
                        </Button>
                        <Button className="btn btn-primary mt-6" onClick={this.leaveTournament}>
                            Leave
                        </Button>
                        </div>
                    ) : (
                        <>
                            <Link className="btn btn-primary mt-6" to={`/update-tournament/${tournament.name}`}>
                                Update
                            </Link>
                            <Button className="btn btn-primary mt-6" onClick={this.startTournament} disabled={tournament.currentRound !== 0}>
                                Start tournament
                            </Button>
                            <Button className="btn btn-primary mt-6" onClick={this.startNextRound} disabled={tournament.currentRound === tournament.maxRounds}>
                                Start next round
                            </Button>
                            <Button className="btn btn-primary mt-6" onClick={this.completeTournament} disabled={status === 'COMPLETED'}>
                                Complete tournament
                            </Button>
                        </>
                    )}
                </Card>
            </div>
        );
    }
}

const TournamentDetailsWithParams = (props) => (
    <TournamentDetails {...props} params={useParams()} />
);

export default withNavigateandLocation(TournamentDetailsWithParams);
