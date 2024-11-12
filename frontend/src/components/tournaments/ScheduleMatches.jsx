import React, { Component } from 'react';
import { Button } from '../ui/button';
import { Link, useParams } from 'react-router-dom';
import MatchService from '../../services/MatchService';
import TournamentService from '../../services/TournamentService';

class ScheduleMatches extends Component {
    constructor(props) {
        super(props);
        this.state = {
            matches: [],
            tournament: '',
            scheduledTimes: {},
        };
    }

    async componentDidMount() {
        await this.fetchMatches();
        await this.fetchTournament();
    }

    fetchTournament = async () => {
        const { id } = this.props.params;
        try {
            const res = await TournamentService.getTournamentById(id);
            this.setState({ tournament: res.data });
            console.log(this.state.tournament);
        } catch (error) {
            console.error("Failed to fetch tournament", error);
        }
    };

    fetchMatches = async () => {
        const { id } = this.props.params;
        try {
            const res = await MatchService.getAllMatchesById(id);
            this.setState({ matches: res.data });
            console.log(res.data);
        } catch (error) {
            console.error("Failed to fetch matches", error);
        }
    };

    handleDateTimeChange = (matchId, dateTime) => {
        this.setState((prevState) => ({
            scheduledTimes: { ...prevState.scheduledTimes, [matchId]: dateTime }
        }));
    };

    saveScheduledTime = async (matchId) => {
        const { scheduledTimes } = this.state;
        const scheduledTime = scheduledTimes[matchId];
        
        if (scheduledTime) {
            const localDateTime = new Date(scheduledTime);
            const utcScheduledTime = new Date(localDateTime.getTime() - localDateTime.getTimezoneOffset() * 60000).toISOString(); // Convert local to UTC
            
            try {
                await MatchService.scheduleMatch(matchId, { scheduledTime: utcScheduledTime });
                this.fetchMatches(); // refresh the matches list
            } catch (error) {
                console.error("Failed to schedule match", error);
            }
        } else {
            alert("Please select a valid date and time");
        }
    };

    renderMatchesTable = () => {
        const { matches } = this.state;
        if (!matches.length) return <p>No matches available yet.</p>;

        return (
            <table className="table-auto w-full border-collapse border border-primary my-4 shadow-md">
                <thead className="bg-primary">
                    <tr>
                        <th className="border-primary px-4 py-2">Match ID</th>
                        <th className="border-primary px-4 py-2">Player 1</th>
                        <th className="border-primary px-4 py-2">Player 2</th>
                        <th className="border-primary px-4 py-2">Round</th>
                        <th className="border-primary px-4 py-2">Status</th>
                        <th className="border-primary px-4 py-2">Schedule</th>
                    </tr>
                </thead>
                <tbody>
                    {matches.map((match) => (
                        <tr key={match.id} className="bg-primary">
                            <td className="border-primary px-4 py-2">{match.id}</td>
                            <td className="border-primary px-4 py-2">{match.participantIds[0]}</td>
                            <td className="border-primary px-4 py-2">{match.participantIds[1]}</td>
                            <td className="border-primary px-4 py-2">{match.roundNumber}</td>
                            <td className="border-primary px-4 py-2">{match.status}</td>
                            <td className="border-primary px-4 py-2">{new Date(match.scheduledTime).toLocaleString()}</td>
                            <td className="border-primary px-4 py-2">
                                <div>
                                    <input
                                        type="datetime-local"
                                        value={this.state.scheduledTimes[match.id] || ''}
                                        onChange={(e) => this.handleDateTimeChange(match.id, e.target.value)}
                                        className="border rounded p-1 mr-2"
                                    />
                                    <button
                                        onClick={() => this.saveScheduledTime(match.id)}
                                        className="btn btn-primary ml-2"
                                    >
                                        Save
                                    </button>
                                </div>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        );
    };

    render() {
        const { tournament } = this.state;
        return (
            <div className="p-6 max-w-4xl mx-auto">
                <h2 className="text-3xl font-bold text-center text-primary mb-4">
                    {tournament.name}: Schedule Matches
                </h2>
                
                {this.renderMatchesTable()}

                <div className="flex justify-center mt-6">
                    <Link to={`/tournaments/${tournament.name}`}>
                        <button className="btn btn-primary">Back to Tournament Details</button>
                    </Link>
                </div>
            </div>
        );
    }
}

const ScheduleMatchesWithParams = (props) => (
    <ScheduleMatches {...props} params={useParams()} />
);

export default ScheduleMatchesWithParams;
