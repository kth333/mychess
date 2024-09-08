import React from 'react';
// import { ChessIcon } from 'lucide-react';
import { Button } from "./ui/button";
import { Link } from 'react-router-dom';

class NavBar extends React.Component {
  render() {
    return (
      <header className="flex items-center justify-between px-6 py-4 bg-background border-b">
        <div className="flex items-center space-x-2">
          {/* <ChessIcon className="h-8 w-8" /> */}
          <span className="text-2xl font-bold">
                <Link to="/">myChess</Link>
            </span>
        </div>
        <nav className="space-x-4">
          <Button variant="ghost" asChild>
            <Link to="/login">Login</Link>
          </Button>
          <Button asChild>
            <Link to="/register">Register</Link>
          </Button>
        </nav>
      </header>
    );
  }
}

export default NavBar;