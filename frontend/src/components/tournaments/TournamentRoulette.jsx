import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import { ArrowLeftIcon, ArrowRightIcon } from "lucide-react";

class TournamentRoulette extends Component {
  constructor(props) {
    super(props);
    this.state = {
      tournaments: [
        { title: "Summer Blitz", date: "July 15-16, 2025", image: "https://images.unsplash.com/photo-1604948501466-4e9c339b9c24?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Nnx8Y2hlc3MlMjBiYWNrZ3JvdW5kfGVufDB8fDB8fHww" },
        { title: "Grandmaster Open", date: "August 5-7, 2025", image: "https://png.pngtree.com/thumb_back/fw800/background/20230913/pngtree-chess-pieces-on-a-checkered-chess-board-image_13252137.jpg" },
        { title: "Junior Championship", date: "September 1-3, 2025", image: "https://img.freepik.com/premium-photo/chess-background-with-golden-chess-pieces-vector-illustration-your-design_934652-2399.jpg" },
        // Add more tournament objects here
      ],
      currentIndex: 0,
    };
  }

  componentDidMount() {
    this.autoSwipe = setInterval(this.nextTournament, 4000); // Change every 4 seconds
  }

  componentWillUnmount() {
    clearInterval(this.autoSwipe); // Clear interval on unmount
  }

  nextTournament = () => {
    this.setState(prevState => ({
      currentIndex: (prevState.currentIndex + 1) % prevState.tournaments.length,
    }));
  };

  prevTournament = () => {
    this.setState(prevState => ({
      currentIndex: (prevState.currentIndex - 1 + prevState.tournaments.length) % prevState.tournaments.length,
    }));
  };

  render() {
    const { tournaments, currentIndex } = this.state;

    return (
      <section className="relative w-full h-96"> {/* Increase height here */}
        <h2 className="text-3xl text-white font-bold text-center mb-8 absolute top-5 left-0 right-0 z-20"> {/* Absolute positioning */}
          Featured Tournaments
        </h2>
        
        <div className="flex justify-center items-center relative w-full h-full"> {/* Ensure relative positioning */}
          <button onClick={this.prevTournament} className="absolute left-0 z-10">
            <ArrowLeftIcon className="h-6 w-6" />
          </button>

          <div className="overflow-hidden w-full relative h-full">
            <div
              className="flex transition-transform duration-500"
              style={{ transform: `translateX(-${currentIndex * 100}%)`, height: '100%' }} // Maintain full height
            >
              {tournaments.map((tournament, index) => (
                <div
                  key={index}
                  className="min-w-full h-full bg-cover bg-center"
                  style={{ backgroundImage: `url(${tournament.image})` }}
                >
                  <div className="flex flex-col items-center justify-center h-full bg-black bg-opacity-40 text-white">
                    <h3 className="text-2xl font-semibold">{tournament.title}</h3>
                    <p className="mb-4">{tournament.date}</p>
                    <Link className="btn btn-accent" to={`/tournaments/${index + 1}`}>
                      View Details
                    </Link>
                  </div>
                </div>
              ))}
            </div>
          </div>

          <button onClick={this.nextTournament} className="absolute right-0 z-10">
            <ArrowRightIcon className="h-6 w-6" />
          </button>
        </div>
      </section>
    );
  }
}

export default TournamentRoulette;
