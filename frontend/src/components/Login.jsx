import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import { Link } from "react-router-dom";
import AuthService from '../services/AuthService';

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    let credentials = {
      username: this.state.username,
      password: this.state.password,
      
    };
    console.log("credentials  => " + JSON.stringify(credentials))

    try {
        await AuthService.loginUser(credentials).then((res) => {
          if (res.data) {
            console.log("login success");
            console.log(res.data);
            localStorage.setItem("token", res.data.token);
          }});
        
      } catch (error) {
        console.error('login error:', error.response ? error.response.data : error.message);
      }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { username, password } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <form className="bg-white p-8 rounded-lg shadow-lg w-96" onSubmit={this.handleSubmit}>
          <h2 className="text-2xl font-bold text-center mb-6">Login</h2>
          <div className="mb-4">
            <label className="block text-gray-700 text-sm font-bold mb-2">Username</label>
            <input
              type="username"
              name="username"
              value={username}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="mb-6">
            <label className="block text-gray-700 text-sm font-bold mb-2">Password</label>
            <input
              type="password"
              name="password"
              value={password}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>
          <div className="flex flex-col space-y-4">
                <button
                    type="submit"
                    className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
                >
                    Login
                </button>
                <Link to="/register">
                    <button
                        type="submit"
                        className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
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
