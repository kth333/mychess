import React, { Component } from 'react';
import { Link } from "react-router-dom";
import { Button } from "./ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon } from "lucide-react";
import withNavigateandLocation from './withNavigateandLocation';
import TournamentRoulette from './tournaments/TournamentRoulette';
import PlayerService from '../services/PlayerService';
import TournamentService from '../services/TournamentService';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      mouseX: 0,
      mouseY: 0,
      leaderboardData: [],
      upcomingTournaments: [],
    };
  }

  componentDidMount() {
    window.addEventListener('mousemove', this.handleMouseMove);
    this.fetchLeaderboardData();
    this.fetchUpcomingTournaments();
  }

  componentWillUnmount() {
    window.removeEventListener('mousemove', this.handleMouseMove);
  }

  handleMouseMove = (event) => {
    this.setState({
      mouseX: event.clientX,
      mouseY: event.clientY,
    });
  };

  fetchLeaderboardData = async () => {
    try {
      const res = await PlayerService.getLeaderboard();
      this.setState({ leaderboardData: res.data });
    } catch (error) {
      console.error("Error fetching leaderboard data:", error);
    }
  };

  fetchUpcomingTournaments = async () => {
    try {
      const res = await TournamentService.getUpcomingTournaments(1);
      this.setState({ upcomingTournaments: res.data.content });
    } catch (error) {
      console.error("Error fetching upcoming tournaments:", error);
    }
  }

  render() {
    const { mouseX, mouseY, leaderboardData, upcomingTournaments } = this.state;
    const theme = sessionStorage.getItem('theme');

    const minOpacity = 0.4;
    const maxOpacity = 0.9;
    const mouseXRatio = mouseX / window.innerWidth;
    const mouseYRatio = mouseY / window.innerHeight;
    const opacityX = minOpacity + (maxOpacity - minOpacity) * mouseXRatio;
    const opacityY = minOpacity + (maxOpacity - minOpacity) * mouseYRatio;

    const gradient = theme === 'light'
        ? `linear-gradient(to right, rgba(34, 34, 34, ${opacityX}), rgba(0, 51, 102, ${opacityY}))`
        : `linear-gradient(to right, rgba(0, 128, 255, ${opacityX}), rgba(128, 0, 255, ${opacityY}))`;

    return (
        <div className="flex flex-col min-h-screen">
          <main className="flex-grow">
            <section
                className="py-20 px-6 text-center text-white"
                style={{ background: gradient }}
            >
              <h1 className="text-4xl font-bold mb-4">Welcome to MyChess</h1>
              <p className="text-xl mb-8">Manage and participate in chess tournaments with ease</p>
              <Link className="btn btn-primary w-auto font-bold" to="/tournaments/page/1">
                Explore Tournaments
              </Link>
            </section>
            <TournamentRoulette className="w-full"/>

            {/* Leaderboard Section */}
            <section className="py-16 px-6">
              <h2 className="text-3xl font-bold text-center mb-12">Global Leaderboard</h2>
              <div className="overflow-x-auto">
                <div className="overflow-y-auto max-h-96">
                  <table className="min-w-full bg-primary rounded-lg">
                    <thead>
                    <tr>
                      <th className="px-4 py-2">Rank</th>
                      <th className="px-4 py-2">Username</th>
                      <th className="px-4 py-2">Title</th>
                      <th className="px-4 py-2">Country</th>
                      <th className="px-4 py-2">Glicko Rating</th>
                    </tr>
                    </thead>
                    <tbody>
                    {Array.from(leaderboardData || []).slice(0, 50).map((player, index) => (
                        <tr key={player.playerId} className="text-center">
                          <td className="px-4 py-2">{index + 1}</td>
                          <td className="px-4 py-2">
                            <Link
                                to={`/profile/${player.playerId}`}
                                className="text-blue-500 hover:text-blue-700 font-semibold underline"
                            >
                              {player.username}
                            </Link>
                          </td>
                          <td className="px-4 py-2">{player.rank || 'N/A'}</td>
                          <td className="px-4 py-2">{player.country}</td>
                          <td className="px-4 py-2">
                            {player.glickoRating !== undefined ? player.glickoRating.toFixed(1) : "N/A"}
                          </td>
                        </tr>
                    ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </section>

            <section className="py-16 px-6">
              <h2 className="text-3xl font-bold text-center mb-12">Upcoming Tournaments</h2>
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {upcomingTournaments.map((tournament, index) => (
                    <Card key={index} className="flex flex-col h-full">
                      <CardHeader>
                        <CardTitle>{tournament.name}</CardTitle>
                        <CardDescription>
                          <CalendarIcon className="inline-block mr-2 h-4 w-4" />
                          {new Date(tournament.startDateTime).toLocaleString(undefined, {
                            year: 'numeric',
                            month: 'long',
                            day: 'numeric',
                            hour: '2-digit',
                            minute: '2-digit',
                          })}
                        </CardDescription>
                      </CardHeader>

                      <CardContent className="flex flex-col flex-grow">
                        <p className="flex items-center mb-4">
                          <UsersIcon className="mr-2 h-4 w-4" />
                          {tournament.maxPlayers} players
                        </p>
                        <div className="mt-auto">
                          <Link className="btn btn-primary w-auto font-bold" to={`/tournaments/${tournament.id}`} variant="outline" asChild>
                            View Details
                          </Link>
                        </div>
                      </CardContent>
                    </Card>
                ))}
              </div>
            </section>

            <section className="py-16 px-6 bg-muted">
              <h2 className="text-3xl font-bold text-center mb-12">Why Choose MyChess?</h2>
              <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div className="text-center">
                  <CalendarIcon className="mx-auto h-12 w-12 mb-4 text-primary" />
                  <h3 className="text-xl font-semibold mb-2">Easy Scheduling</h3>
                  <p>Effortlessly plan and manage tournament dates and rounds</p>
                </div>
                <div className="text-center">
                  <UsersIcon className="mx-auto h-12 w-12 mb-4 text-primary" />
                  <h3 className="text-xl font-semibold mb-2">Player Management</h3>
                  <p>Keep track of participants, rankings, and results</p>
                </div>
                <div className="text-center">
                  <TrophyIcon className="mx-auto h-12 w-12 mb-4 text-primary" />
                  <h3 className="text-xl font-semibold mb-2">Real-time Results</h3>
                  <p>Instantly update and view tournament standings</p>
                </div>
              </div>
            </section>
          </main>
        </div>
    );
  }
}

export default withNavigateandLocation(Home);