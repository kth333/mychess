import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import AuthService from '../services/AuthService';
import countryList from 'react-select-country-list';
import { Terminal } from "lucide-react";
import { Alert, AlertDescription, AlertTitle } from "./ui/alert";
import { jwtDecode } from 'jwt-decode';
import { Link } from 'react-router-dom';

class AuthPage extends Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      role: 'ROLE_PLAYER',
      email: '',
      gender: '',
      country: '',
      region: '',
      city: '',
      birthDate: '',
      countries: countryList().getData(),
      showAlert: false,
      alertType: '',
      alertMessage: '',
      showLogin: true, // Toggle between login and register views
    };
  }

  toggleAuthForm = () => {
    this.setState((prevState) => ({ showLogin: !prevState.showLogin }));
  };

  handleSubmitLogin = async (e) => {
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

          if (token) {
            const decodedToken = jwtDecode(token);
            const role = decodedToken.role[0];
            let playerId;

            if (role === "ROLE_PLAYER") {
              playerId = decodedToken.userId;
              sessionStorage.setItem("currentPlayerId", playerId);
            }
            console.log(`Role: ${role}, Player ID: ${playerId || "N/A"}`);

            sessionStorage.setItem("role", role);
          }

          this.setState({
            showAlert: true,
            alertType: 'success',
            alertMessage: 'Login successful! Redirecting to home page...',
          });
          setTimeout(() => {
            this.setState({ showAlert: false });
            this.props.navigate('/');
          }, 3000);
        }
      });
    } catch (error) {
      const errorMessage = error.response ? error.response.data : error.message;
      console.error('Registration error:', errorMessage);
      this.setState({
        showAlert: true,
        alertType: 'error',
        alertMessage: `Login failed: ${errorMessage}`,
      });
      setTimeout(() => {
        this.setState({ showAlert: false });
      }, 5000);
      console.error('login error:', error.response ? error.response.data : error.message);
    }
  };

  handlePlayerLogin = (e) => {
    this.setState({ role: 'ROLE_PLAYER' }, () => {
      this.handleSubmitLogin(e);
    });
  };

  handleAdminLogin = (e) => {
    this.setState({ role: 'ROLE_ADMIN' }, () => {
      this.handleSubmitLogin(e);
    });
  };

  handleSubmitRegister = async (e) => {
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
    const { username, password, email, gender, country, region, city, birthDate, countries, showAlert, alertType, alertMessage, showLogin } = this.state;

    return (
      <div className="flex min-h-screen justify-center items-center bg-base-100">
        <div className="relative w-full max-w-2xl h-auto p-8 bg-primary-content rounded-xl shadow-lg overflow-hidden">
          {/* Alert Message */}
          {showAlert && (
            <Alert className="fixed bottom-4 right-4 z-50 p-4 max-w-fit w-auto min-w-[200px] rounded-xl shadow-lg bg-accent-content">
              <Terminal className="h-4 w-4" />
              <AlertTitle>{alertType === 'success' ? 'Success!' : 'Error!'}</AlertTitle>
              <AlertDescription>{alertMessage}</AlertDescription>
            </Alert>
          
          )}

          {/* Sliding Form Container */}
          <div className="flex w-[200%] transition-transform duration-500 ease-in-out" style={{ transform: showLogin ? 'translateX(0)' : 'translateX(-50%)' }}>
            {/* Login Form */}
            <div className="w-1/2 p-8 flex flex-col justify-center items-center">
              <form className="space-y-4 w-full" onSubmit={this.handleSubmitLogin}>
                <h2 className="text-2xl font-bold text-center mb-6">Login</h2>
                <input
                  type="text"
                  name="username"
                  placeholder="Username"
                  value={username}
                  onChange={this.handleInputChange}
                  required
                  disabled={!showLogin}
                  className="input input-bordered w-full"
                />
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={password}
                  onChange={this.handleInputChange}
                  required
                  disabled={!showLogin}
                  className="input input-bordered w-full"
                />
                <button type="submit" className="btn btn-outline w-full mt-2" onClick={this.handlePlayerLogin} disabled={!showLogin}>Login</button>
                <button type="button" className="btn btn-outline w-full mt-2" onClick={this.handleAdminLogin} disabled={!showLogin}>Login as Admin</button>
                <div className="flex justify-between mt-4">
                  <button onClick={this.toggleAuthForm} className="btn btn-link" disabled={!showLogin}>Register Now</button>
                  <Link className="btn btn-link" to="/password-reset-request" disabled={!showLogin}>Forget Password?</Link>
                </div>
              </form>
            </div>

            {/* Register Form */}
            <div className="w-1/2 p-8 flex flex-col justify-center items-center">
              <form className="space-y-4 w-full" onSubmit={this.handleSubmitRegister}>
                <h2 className="text-2xl font-bold text-center mb-6">Register</h2>
                <input
                  type="text"
                  name="username"
                  placeholder="Username"
                  value={username}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <input
                  type="email"
                  name="email"
                  placeholder="Email"
                  value={email}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <input
                  type="password"
                  name="password"
                  placeholder="Password"
                  value={password}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <select
                  name="gender"
                  value={gender}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="select select-bordered w-full"
                >
                  <option value="">Select Gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </select>
                <select
                  name="country"
                  value={country}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="select select-bordered w-full"
                >
                  <option value="">Select Country</option>
                  {countries.map((country) => (
                    <option key={country.value} value={country.label}>{country.label}</option>
                  ))}
                </select>
                <input
                  type="text"
                  name="region"
                  placeholder="Region"
                  value={region}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <input
                  type="text"
                  name="city"
                  placeholder="City"
                  value={city}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <input
                  type="date"
                  name="birthDate"
                  value={birthDate}
                  onChange={this.handleInputChange}
                  required
                  disabled={showLogin}
                  className="input input-bordered w-full"
                />
                <button type="submit" className="btn btn-primary w-full" disabled={showLogin}>Register</button>
                <button onClick={this.toggleAuthForm} className="btn btn-link w-full" disabled={showLogin}>Sign In</button>
              </form>
            </div>
          </div>
        </div>
      </div>


    );
  }
}

export default withNavigateandLocation(AuthPage);
