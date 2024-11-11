import React, { Component } from 'react';
import { Button } from './ui/button';
import { Link } from 'react-router-dom';
import ThemeSwitcher from './ThemeSwitcher';
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
  NavigationMenuTrigger,
  navigationMenuTriggerStyle,
} from './ui/navigation-menu';

class NavBar extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoggedIn: !!sessionStorage.getItem('token'),
      role: sessionStorage.getItem('role'),
      playerId: sessionStorage.getItem('currentPlayerId'),
    };
  }

  handleSignOut = () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('role');
    sessionStorage.removeItem('currentPlayerId');
    this.setState({
      isLoggedIn: false,
      role: null,
      playerId: null,
    });
  };

  render() {
    const { isLoggedIn, role, playerId } = this.state;

    return (
        <header className="flex items-center justify-between px-8 py-4 bg-secondary shadow-lg border-b border-gray-300">
          <div className="flex items-center space-x-6">
            <Link to="/" className="text-3xl font-bold text-primary hover:text-accent">
              MyChess
            </Link>
            <ThemeSwitcher />
          </div>
          <nav className="space-x-4">
            <NavigationMenu>
              <NavigationMenuList className="flex items-center space-x-6">

                {!isLoggedIn ? (
                    <>
                      <NavigationMenuItem>
                        <NavigationMenuLink asChild>
                          <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to="/login">
                            Sign in
                          </Link>
                        </NavigationMenuLink>
                      </NavigationMenuItem>
                    </>
                ) : (
                    <>
                      <NavigationMenuItem>
                        <NavigationMenuLink asChild>
                          <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to="/tournaments/page/1">
                            View All Tournaments
                          </Link>
                        </NavigationMenuLink>
                      </NavigationMenuItem>

                      {role === 'ROLE_ADMIN' && (
                          <>
                            <NavigationMenuItem>
                              <NavigationMenuLink asChild>
                                <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to="/whitelist">
                                  Whitelist
                                </Link>
                              </NavigationMenuLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                              <NavigationMenuLink asChild>
                                <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to="/blacklist">
                                  Blacklist
                                </Link>
                              </NavigationMenuLink>
                            </NavigationMenuItem>
                            <NavigationMenuItem>
                              <NavigationMenuLink asChild>
                                <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to="/create-tournament">
                                  Create Tournament
                                </Link>
                              </NavigationMenuLink>
                            </NavigationMenuItem>
                          </>
                      )}

                      {role === 'ROLE_PLAYER' && (
                          <NavigationMenuItem>
                            <NavigationMenuLink asChild>
                              <Link className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md" to={`/profile/${playerId}`}>
                                Profile
                              </Link>
                            </NavigationMenuLink>
                          </NavigationMenuItem>
                      )}

                      <NavigationMenuItem>
                        <NavigationMenuLink asChild>
                          <Link
                              className="text-primary text-lg hover:bg-accent hover:text-accent-foreground px-4 py-2 rounded-md"
                              onClick={this.handleSignOut}
                              to="/"
                          >
                            Sign Out
                          </Link>
                        </NavigationMenuLink>
                      </NavigationMenuItem>
                    </>
                )}
              </NavigationMenuList>
            </NavigationMenu>
          </nav>
        </header>
    );
  }
}

export default NavBar;