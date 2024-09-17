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
      isLoggedIn: !!localStorage.getItem('token'),
      role: localStorage.getItem('role'),
    };
  }

  handleSignOut = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.setState({
      isLoggedIn: false,
      role: null,
    });
  };

  render() {
    const { isLoggedIn, role } = this.state;

    return (
      <header className="flex items-center justify-between px-6 py-4 bg-secondary border-b border-accent">
        <div className="flex items-center space-x-4">
          <span className="text-2xl font-bold flex items-center space-x-2">
            <Link to="/" className="text-primary">MyChess</Link>
            <ThemeSwitcher />
          </span>
        </div>
        <nav className="space-x-4">
          <NavigationMenu>
            <NavigationMenuList>
              

              {/* Conditional Links */}
              {!isLoggedIn ? (
                <>
                  <NavigationMenuItem>
                    <NavigationMenuLink asChild>
                      <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/login">Login</Link>
                    </NavigationMenuLink>
                  </NavigationMenuItem>
                  <NavigationMenuItem>
                    <NavigationMenuLink asChild>
                      <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/register">Register</Link>
                    </NavigationMenuLink>
                  </NavigationMenuItem>
                </>
              ) : (
                <>
                  {role === 'ROLE_ADMIN' && (
                    <NavigationMenuItem>
                      <NavigationMenuLink asChild>
                        <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/create-tournament">Create Tournament</Link>
                      </NavigationMenuLink>
                      <NavigationMenuLink asChild>
                        <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/tournaments">View All Tournaments</Link>
                      </NavigationMenuLink>
                    </NavigationMenuItem>
                    
                  )}
                  {role === 'ROLE_PLAYER' && (
                    <NavigationMenuItem>
                      <NavigationMenuLink asChild>
                        <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/tournaments">View All Tournaments</Link>
                      </NavigationMenuLink>
                      <NavigationMenuLink asChild>
                        <Link className="text-primary hover:bg-accent hover:text-accent-foreground rounded-md px-4 py-2" to="/profile">Profile</Link>
                      </NavigationMenuLink>
                    </NavigationMenuItem>
                    
                  )}
                  <NavigationMenuItem>
                    <NavigationMenuLink asChild>
                      <Button variant="ghost" className="text-primary hover:bg-accent hover:text-accent-foreground" onClick={this.handleSignOut}>
                        Sign Out
                      </Button>
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
