import React, { Component }from 'react';

import { Button } from "./ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { CalendarIcon, TrophyIcon, UsersIcon } from "lucide-react";
import withNavigateandLocation from './withNavigateandLocation';

class Home extends Component {
  render() {
    return (
      <div className="flex flex-col min-h-screen">

        <main className="flex-grow">
          <section className="py-20 px-6 text-center bg-gradient-to-r from-blue-500 to-purple-600 text-white">
            <h1 className="text-4xl font-bold mb-4">Welcome to MyChess</h1>
            <p className="text-xl mb-8">Manage and participate in chess tournaments with ease</p>
            <Button size="lg" asChild>
              <a href="/tournaments">Explore Tournaments</a>
            </Button>
          </section>

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
                    <Button className="mt-4" variant="outline" asChild>
                      <a href={`/tournaments/${index + 1}`}>View Details</a>
                    </Button>
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

export default withNavigateandLocation(Home);