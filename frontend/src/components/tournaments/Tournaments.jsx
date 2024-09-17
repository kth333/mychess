import React, { Component } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon } from "lucide-react";
import withNavigateandLocation from '../withNavigateandLocation';
import Pagination from "../ui/Pagination"  // Import the Pagination component
import TournamentService from '../../services/TournamentService';

class Tournaments extends Component {
  constructor(props) {
    super(props);
    this.state = {
      searchQuery: '',
      currentPage: 1,
      tournamentsPerPage: 9,
      tournaments: [],
    };
  }

  componentDidMount() {
    this.fetchData();
  }

  fetchData = async () => {
    try {
      const response = await TournamentService.getAllTournaments();
      this.setState({ tournaments: response.data });
    } catch (error) {
      console.error('Error fetching tournaments:', error.response ? error.response.data : error.message);
    }
  };

  handleSearchChange = (event) => {
    this.setState({ searchQuery: event.target.value, currentPage: 1 });
  };

  filterTournaments = (tournaments) => {
    const { searchQuery } = this.state;
    if (!searchQuery) return tournaments;
    return tournaments.filter(tournament =>
      tournament.name.toLowerCase().includes(searchQuery.toLowerCase())
    );
  };

  paginateTournaments = (tournaments) => {
    const { currentPage, tournamentsPerPage } = this.state;
    const startIndex = (currentPage - 1) * tournamentsPerPage;
    const endIndex = startIndex + tournamentsPerPage;
    return tournaments.slice(startIndex, endIndex);
  };

  handlePageChange = (pageNumber) => {
    this.setState({ currentPage: pageNumber });
  };

  render() {
    const { tournaments, searchQuery, currentPage, tournamentsPerPage } = this.state;

    const filteredTournaments = this.filterTournaments(tournaments);
    const paginatedTournaments = this.paginateTournaments(filteredTournaments);

    return (
      <div className="flex flex-col min-h-screen">
        <main className="flex-grow">
          <section className="py-20 px-6 text-center bg-gradient-to-r from-blue-500 to-purple-600 text-white">
            <h1 className="text-4xl font-bold mb-4">All Tournaments</h1>
            <p className="text-xl mb-8">Explore and view details of all available chess tournaments</p>
            <div className="mb-8">
              <input
                type="text"
                placeholder="Search tournaments..."
                className="p-2 border rounded-md w-full max-w-md mx-auto"
                value={searchQuery}
                onChange={this.handleSearchChange}
              />
            </div>
          </section>

          <section className="py-16 px-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {paginatedTournaments.map((tournament) => (
                <Card key={tournament.id}>
                  <CardHeader>
                    <CardTitle>{tournament.name}</CardTitle>
                    <CardDescription>
                      <CalendarIcon className="inline-block mr-2 h-4 w-4" />
                      {new Date(tournament.startDateTime).toLocaleDateString()} - {new Date(tournament.endDateTime).toLocaleDateString()}
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <p className="flex items-center">
                      <UsersIcon className="mr-2 h-4 w-4" />
                      {tournament.participants.length} participants
                    </p>
                    <p className="flex items-center mt-2">
                      <TrophyIcon className="mr-2 h-4 w-4 text-yellow-500" />
                      Rating Range: {tournament.minRating} - {tournament.maxRating}
                    </p>
                    <button className="mt-4 btn btn-primary w-auto font-bold" variant="outline" asChild>
                      <a href={`/tournaments/${tournament.id}`}>View Details</a>
                    </button>
                  </CardContent>
                </Card>
              ))}
            </div>
            <Pagination
              currentPage={currentPage}
              totalItems={filteredTournaments.length}
              itemsPerPage={tournamentsPerPage}
              onPageChange={this.handlePageChange}
            />
          </section>
        </main>

        <footer className="bg-background border-t py-6 px-6 text-center">
          <p>&copy; 2023 MyChess. All rights reserved.</p>
          <div className="mt-2">
            <a href="/about" className="text-primary hover:underline">About</a>
            <span className="mx-2">|</span>
            <a href="/contact" className="text-primary hover:underline">Contact</a>
            <span className="mx-2">|</span>
            <a href="/privacy" className="text-primary hover:underline">Privacy Policy</a>
          </div>
        </footer>
      </div>
    );
  }
}

export default withNavigateandLocation(Tournaments);
