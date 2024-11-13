import React, { Component } from 'react';
import { Link, useParams } from "react-router-dom";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon } from "lucide-react";
import withNavigateandLocation from '../withNavigateandLocation';
import Pagination from "../ui/Pagination";  // Import the Pagination component
import TournamentService from '../../services/TournamentService';

class Tournaments extends Component {
  constructor(props) {
    super(props);
    this.state = {
      searchQuery: '',
      currentPage: 1,
      tournamentsPerPage: 9,
      tournaments: [],
      totalItems: 0,  // Total items from the backend
      totalPages: 0,  // Total pages from the backend
      mouseX: 0,
      mouseY: 0,
    };
  }

  componentDidMount() {
    this.fetchData();
    window.addEventListener('mousemove', this.handleMouseMove);
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

  fetchData = async () => {
    const currentPage = this.props.params.pageNumber || 1;
    console.log(currentPage);
    try {
      const response = await TournamentService.getAllTournaments(currentPage - 1);
      console.log(response.data);
      // Assuming the response data includes pagination metadata
      const { content, totalElements, totalPages } = response.data;

      this.setState({
        tournaments: content,
        totalItems: totalElements,
        totalPages: totalPages,
        currentPage: currentPage,
      });
    } catch (error) {
      console.error('Error fetching tournaments:', error.response ? error.response.data : error.message);
    }
  };

  handleSearchChange = (event) => {
    this.setState({ searchQuery: event.target.value, currentPage: 1 }, this.fetchData);
  };

  handlePageChange = (pageNumber) => {
    this.setState({ currentPage: pageNumber }, () => {

      this.props.navigate(`/tournaments/page/${pageNumber}`);
      window.location.reload();
    });

  };

  render() {
    const { tournaments, searchQuery, currentPage, totalItems, tournamentsPerPage, mouseX, mouseY } = this.state;

    const userRole = sessionStorage.getItem("role");
    const isAdmin = userRole === 'ROLE_ADMIN';

    // Define gradients based on the theme
    let gradient;

    // Define the opacity limits
    const minOpacity = 0.4;
    const maxOpacity = 0.9;

    // Calculate the mouse positions as ratios
    const mouseXRatio = mouseX / window.innerWidth;
    const mouseYRatio = mouseY / window.innerHeight;

    // Constrain the opacity values using the defined limits
    const opacityX = minOpacity + (maxOpacity - minOpacity) * mouseXRatio;
    const opacityY = minOpacity + (maxOpacity - minOpacity) * mouseYRatio;

    const theme = sessionStorage.getItem('theme');
    if (theme === 'light') {
      gradient = `linear-gradient(to right, rgba(34, 34, 34, ${opacityX}), rgba(0, 51, 102, ${opacityY}))`;
    } else {
      gradient = `linear-gradient(to right, rgba(0, 128, 255, ${opacityX}), rgba(128, 0, 255, ${opacityY}))`;
    }

    return (
        <div className="flex flex-col min-h-screen">
          <main className="flex-grow">
            <section className="py-20 px-6 text-center text-white" style={{ background: gradient }}>
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
                {Array.from(tournaments || []).map((tournament) => (
                    <Card key={tournament.id} className="h-full flex flex-col">
                      <CardHeader>
                        <CardTitle>{tournament.name}</CardTitle>
                      </CardHeader>

                      <CardContent className="flex flex-col flex-grow">
                        <div className="flex-grow"></div> {/* Spacer to push bottom content down */}

                        {/* Grouped participants, rating, and button at the bottom */}
                        <div className="space-y-4">
                          <p className="flex items-center">
                            <CalendarIcon className="inline-block mr-2 h-4 w-4" />
                            {new Date(tournament.startDateTime).toLocaleDateString()} - {new Date(tournament.endDateTime).toLocaleDateString()}
                          </p>
                          <p className="flex items-center">
                            <UsersIcon className="mr-2 h-4 w-4" />
                            {tournament.participants.length} participants
                          </p>
                          <p className="flex items-center">
                            <TrophyIcon className="mr-2 h-4 w-4 text-yellow-500" />
                            Rating Range: {tournament.minRating} - {tournament.maxRating}
                          </p>

                          <div>
                            {isAdmin ? (
                                <Link className="btn btn-primary w-full font-bold" to={`/tournaments/${tournament.name}`}>
                                  Manage
                                </Link>
                            ) : (
                                <Link className="btn btn-primary w-full font-bold" to={`/tournaments/${tournament.name}`}>
                                  View Details
                                </Link>
                            )}
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                ))}
              </div>
              <Pagination
                  currentPage={currentPage}
                  totalItems={totalItems}
                  itemsPerPage={tournamentsPerPage}
                  onPageChange={this.handlePageChange}
              />
            </section>
          </main>
        </div>
    );
  }
}

export default withNavigateandLocation(Tournaments);
