import React, { Component } from 'react';
import { Button } from "./ui/button";
import { Link } from 'react-router-dom';
import ThemeSwitcher from './ThemeSwitcher';

class NavBar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoggedIn: !!localStorage.getItem("token"),
      role: localStorage.getItem("role"),
    };
  }

  handleSignOut = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    this.setState({
      isLoggedIn: false,
      role: null,
    });
  };

  render() {
    const { isLoggedIn, role } = this.state;

    return (
      <header className="flex items-center justify-between px-6 py-4 bg-background border-b">
        <div className="flex items-center space-x-4">
          <span className="text-2xl font-bold flex items-center space-x-2">
            <Link to="/">MyChess</Link>
            <ThemeSwitcher />
          </span>
        </div>
        <nav className="space-x-4">
          {!isLoggedIn ? (
            <>
              <Button variant="ghost" asChild>
                <Link to="/login">Login</Link>
              </Button>
              <Button variant="ghost" asChild>
                <Link to="/register">Register</Link>
              </Button>
            </>
          ) : (
            <>
              {role === 'ADMIN' && (
                <Button variant="ghost" asChild>
                  <Link to="/create-tournament">Create Tournament</Link>
                </Button>
              )}
              {role === 'PLAYER' && (
                <Button variant="ghost" asChild>
                  <Link to="/tournaments">View All Tournaments</Link>
                </Button>
              )}
              <Button variant="ghost" onClick={this.handleSignOut}>
                Sign Out
              </Button>
            </>
          )}
        </nav>
      </header>
    );
  }
}

export default NavBar;
