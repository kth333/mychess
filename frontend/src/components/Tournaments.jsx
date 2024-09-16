import React, { Component } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon, StarIcon } from "lucide-react";
import withNavigateandLocation from './withNavigateandLocation';
import Pagination from './Pagination';  // Import the Pagination component

class Tournaments extends Component {
  constructor(props) {
    super(props);
    this.state = {
      searchQuery: '',
      currentPage: 1,
      tournamentsPerPage: 9,
    };
  }

  handleSearchChange = (event) => {
    this.setState({ searchQuery: event.target.value, currentPage: 1 });
  };

  filterTournaments = (tournaments) => {
    const { searchQuery } = this.state;
    if (!searchQuery) return tournaments;
    return tournaments.filter(tournament =>
      tournament.title.toLowerCase().includes(searchQuery.toLowerCase())
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
    // Sample data for tournaments
    const tournaments = [
      { title: "Summer Blitz (Test)", date: "July 15-16, 2023", players: 64, rating: 4.5 },
      { title: "Grandmaster Open (Test)", date: "August 5-7, 2023", players: 128, rating: 4.8 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      // Add more sample tournaments if needed
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },
      { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32, rating: 4.2 },

    ];

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
                value={this.state.searchQuery}
                onChange={this.handleSearchChange}
              />
            </div>
          </section>

          <section className="py-16 px-6">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {paginatedTournaments.map((tournament, index) => (
                <Card key={index}>
                  <CardHeader>
                    <CardTitle>{tournament.title}</CardTitle>
                    <CardDescription>
                      <CalendarIcon className="inline-block mr-2 h-4 w-4" />
                      {tournament.date}
                    </CardDescription>
                  </CardHeader>
                  <CardContent>
                    <p className="flex items-center">
                      <UsersIcon className="mr-2 h-4 w-4" />
                      {tournament.players} players
                    </p>
                    <p className="flex items-center mt-2">
                      <StarIcon className="mr-2 h-4 w-4 text-yellow-500" />
                      Rating: {tournament.rating} / 5
                    </p>
                    <button className="mt-4 btn btn-primary w-auto font-bold" variant="outline" asChild>
                      <a href={`/tournaments/${index + 1}`}>View Details</a>
                    </button>
                  </CardContent>
                </Card>
              ))}
            </div>
            <Pagination
              currentPage={this.state.currentPage}
              totalItems={filteredTournaments.length}
              itemsPerPage={this.state.tournamentsPerPage}
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
