import React, { Component } from 'react';
import { Link } from "react-router-dom";
import { Button } from "./ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon } from "lucide-react";
import withNavigateandLocation from './withNavigateandLocation';
import TournamentRoulette from './tournaments/TournamentRoulette';

class Home extends Component {
  constructor(props) {
    super(props);
    this.state = {
      mouseX: 0,
      mouseY: 0,
    };
  }

  componentDidMount() {
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

  render() {
    const { mouseX, mouseY } = this.state;
    const theme = sessionStorage.getItem('theme');

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

    if (theme === 'light') {
      gradient = `linear-gradient(to right, rgba(34, 34, 34, ${opacityX}), rgba(0, 51, 102, ${opacityY}))`;
    } else {
      gradient = `linear-gradient(to right, rgba(0, 128, 255, ${opacityX}), rgba(128, 0, 255, ${opacityY}))`;
    }


    return (
      <div className="flex flex-col min-h-screen">
        <main className="flex-grow">
          <section 
            className="py-20 px-6 text-center text-white"
            style={{ background: gradient }} // Apply the gradient dynamically
          >
            <h1 className="text-4xl font-bold mb-4">Welcome to MyChess</h1>
            <p className="text-xl mb-8">Manage and participate in chess tournaments with ease</p>
            <Link className="btn btn-primary w-auto font-bold" to="/tournaments/page/1">
              Explore Tournaments
            </Link>
          </section>
          <TournamentRoulette className="w-full"/>

          <section className="py-16 px-6">
            <h2 className="text-3xl font-bold text-center mb-12">Upcoming Tournaments</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {[
                { title: "Summer Blitz (Test)", date: "July 15-16, 2023", players: 64 },
                { title: "Grandmaster Open (Test)", date: "August 5-7, 2023", players: 128 },
                { title: "Junior Championship (Test)", date: "September 1-3, 2023", players: 32 },
              ].map((tournament, index) => (
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
                    <Link className="mt-4 btn btn-primary w-auto font-bold" to={`/tournaments/${index + 1}`} variant="outline" asChild>
                      View Details
                    </Link>
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
