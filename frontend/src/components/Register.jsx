import React, { Component } from 'react';
import AuthService from "../services/AuthService";
import countryList from 'react-select-country-list';
import { Terminal } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "./ui/alert";
import withNavigateandLocation from './withNavigateandLocation';

class Register extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      email: '',
      gender: '',
      country: '',
      region: '',
      city: '',
      birthDate: '',
      countries: countryList().getData(),
      showAlert: false,
      alertType: '', // 'success' or 'error'
      alertMessage: '', // Custom alert message
    };
  }

  handleSubmit = async (e) => {
    e.preventDefault();
    const user = {
      username: this.state.username,
      password: this.state.password,
      email: this.state.email,
      gender: this.state.gender,
      country: this.state.country,
      region: this.state.region,
      city: this.state.city,
      birthDate: this.state.birthDate,
    };

    console.log("user => " + JSON.stringify(user));

    try {
      const res = await AuthService.registerUser(user);
      if (res.data) {
        this.setState({
          showAlert: true,
          alertType: 'success',
          alertMessage: 'Registration successful! Please check your email to verify your account.',
        });
        setTimeout(() => {
          this.setState({ showAlert: false });
          this.props.navigate('/login');
        }, 3000);
      }
    } catch (error) {
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Registration error:', errorMessage);
      this.setState({
        showAlert: true,
        alertType: 'error',
        alertMessage: `Registration failed: ${errorMessage}`,
      });
      setTimeout(() => {
        this.setState({ showAlert: false });
      }, 5000);
    }
  };

  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { username, password, email, gender, country, region, city, birthDate, countries, showAlert, alertType, alertMessage } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="relative w-full max-w-md">

          {/* Alert as overlay */}
          {showAlert && (
            <Alert variant="destructive"
              className={`fixed top-4 left-1/2 transform -translate-x-1/2 z-50 p-4 max-w-lg w-full shadow-lg}`}
            >
              <Terminal className="h-4 w-4" />
              <AlertTitle>{alertType === 'success' ? 'Success!' : 'Error!'}</AlertTitle>
              <AlertDescription>
                {alertMessage}
              </AlertDescription>
            </Alert>
          )}

          {/* Register Form */}
          <form className="p-8 rounded-lg border border-accent shadow-lg bg-secondary" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6">Register</h2>
            
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

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Email</label>
              <input
                type="email"
                name="email"
                value={email}
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

            {/* Gender Dropdown with default "Select Gender" */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Gender</label>
              <select
                name="gender"
                value={gender}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select Gender</option> {/* Default option */}
                <option value="Male">Male</option>
                <option value="Female">Female</option>
                <option value="Other">Other</option>
              </select>
            </div>

            {/* Country Dropdown */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Country</label>
              <select
                name="country"
                value={country}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select a country</option>
                {countries.map((country) => (
                  <option key={country.value} value={country.label}>
                    {country.label}
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Region</label>
              <input
                type="text"
                name="region"
                value={region}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">City</label>
              <input
                type="text"
                name="city"
                value={city}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Birth Date</label>
              <input
                type="date"
                name="birthDate"
                value={birthDate}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            
            <button
              type="submit"
              className="btn btn-primary w-full font-bold"
            >
              Register
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default withNavigateandLocation(Register);
