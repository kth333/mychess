import React, { Component } from 'react';
import { Button } from "./ui/button";
import { Link } from 'react-router-dom';
import ThemeSwitcher from './ThemeSwitcher';

class NavBar extends Component {
  render() {
    return (
      <header className="flex items-center justify-between px-6 py-4 bg-background border-b">
        <div className="flex items-center space-x-4">
          {/* <ChessIcon className="h-8 w-8" /> */}
          <span className="text-2xl font-bold flex items-center space-x-2">
            <Link to="/">MyChess</Link>
            <ThemeSwitcher />
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
