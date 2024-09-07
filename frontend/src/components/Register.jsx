import React, { Component, FormEvent } from 'react';
import { Link } from 'react-router-dom';
import AuthService from "../services/AuthService";

class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      email: '',
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    let user = {
        username: this.state.username,
        password: this.state.password,
        email: this.state.email,
        
    };
    console.log("user  => " + JSON.stringify(user))

    try {
        const response = await AuthService.registerUser(user);
        console.log('Registration successful:', response.data);
      } catch (error) {
        console.error('Registration error:', error.response ? error.response.data : error.message);
      }

  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { username, password, email } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <div className="relative w-full max-w-md">
          {/* Back Arrow */}
          <Link
            to="/"
            className="absolute top-4 left-4 text-gray-500 hover:text-gray-700 focus:outline-none"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M15 19l-7-7 7-7"
              />
            </svg>
          </Link>

          {/* Register Form */}
          <form className="bg-white p-8 rounded-lg shadow-lg" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6">Register</h2>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">Username</label>
              <input
                type="text"
                name="username"
                value={username}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="mb-4">
              <label className="block text-gray-700 text-sm font-bold mb-2">Email</label>
              <input
                type="email"
                name="email"
                value={email}
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
            <button
              type="submit"
              className="w-full bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
            >
              Register
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default Register;
