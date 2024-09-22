import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import { Link } from "react-router-dom";
import AuthService from '../services/AuthService';
import { jwtDecode } from 'jwt-decode';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      role: 'ROLE_PLAYER', // Default to ROLE_PLAYER for regular login
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    let credentials = {
      username: this.state.username,
      password: this.state.password,
      role: this.state.role,
    };

    console.log("credentials  => " + JSON.stringify(credentials));

    try {
      await AuthService.loginUser(credentials).then((res) => {
        if (res.data) {
          console.log("login success");
          console.log(res.data);
          sessionStorage.setItem("token", res.data.token);
          const token = sessionStorage.getItem('token');
          
          // Decoding token to get role
          if (token) {
            const decodedToken = jwtDecode(token);
            console.log(decodedToken.role[0]);
            const role = decodedToken.role[0];
            sessionStorage.setItem("role", role);
          }

          // Navigate after login success
          this.props.navigate('/');
        }
      });
    } catch (error) {
      console.error('login error:', error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  // Set role to ROLE_PLAYER for regular login
  handlePlayerLogin = (e) => {
    this.setState({ role: 'ROLE_PLAYER' }, () => {
      this.handleSubmit(e);
    });
  };

  // Set role to ROLE_ADMIN for admin login
  handleAdminLogin = (e) => {
    this.setState({ role: 'ROLE_ADMIN' }, () => {
      this.handleSubmit(e);
    });
  };

  render() {
    const { username, password } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary">
          <h2 className="text-2xl font-bold text-center mb-6">Login</h2>

          <div className="mb-4">
            <label className="block text-primary text-sm font-bold mb-2">Username</label>
            <input
              type="text"
              name="username"
              value={username}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mb-6">
            <label className="block text-primary text-sm font-bold mb-2">Password</label>
            <input
              type="password"
              name="password"
              value={password}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="flex flex-col space-y-4">
            <button
              type="button"
              className="btn btn-primary w-full font-bold"
              onClick={this.handlePlayerLogin} // Set role to ROLE_PLAYER
            >
              Login
            </button>

            <button
              type="button"
              className="btn btn-primary w-full font-bold"
              onClick={this.handleAdminLogin} // Set role to ROLE_ADMIN
            >
              Login as Admin
            </button>

            <Link to="/register">
              <button
                type="button"
                className="btn btn-primary w-full font-bold"
              >
                Register
              </button>
            </Link>
          </div>
        </form>
      </div>
    );
  }
}

export default withNavigateandLocation(Login);
