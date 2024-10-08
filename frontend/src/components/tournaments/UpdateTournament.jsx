import React, { Component } from 'react';
import TournamentService from '../../services/TournamentService';
import countryList from 'react-select-country-list'; // Use this package for country list
import { useParams } from 'react-router-dom';
import withNavigateandLocation from '../withNavigateandLocation';

class UpdateTournament extends Component {
  constructor(props) {
    super(props);
    this.state = {
      id: '',
      name: '',
      description: '',
      startDateTime: '',
      endDateTime: '',
      registrationStartDate: '',
      registrationEndDate: '',
      format: '',
      status: '',
      minRating: '',
      maxRating: '',
      affectsRating: false,
      minAge: '',
      maxAge: '',
      requiredGender: '',
      country: '',
      region: '',
      city: '',
      address: '',
      timeControlBase: 10, // Default value for base time in minutes
      timeControlIncrement: 5, // Default value for increment in seconds
      countries: countryList().getData(), // Generate country list
    };
  }

  componentDidMount() {
    const { name } = this.props.params; // Assuming you're using React Router
    this.getTournament(name);
  }

  async getTournament(tournamentName) {
    try {
      const response = await TournamentService.getTournamentByName(tournamentName);
      const tournament = response.data;

      this.setState({
        id: tournament.id,
        name: tournament.name,
        description: tournament.description,
        startDateTime: tournament.startDateTime,
        endDateTime: tournament.endDateTime,
        registrationStartDate: tournament.registrationStartDate,
        registrationEndDate: tournament.registrationEndDate,
        format: tournament.format,
        status: tournament.status,
        minRating: tournament.minRating,
        maxRating: tournament.maxRating,
        affectsRating: tournament.affectsRating,
        minAge: tournament.minAge,
        maxAge: tournament.maxAge,
        requiredGender: tournament.requiredGender,
        country: tournament.country,
        region: tournament.region,
        city: tournament.city,
        address: tournament.address,
        timeControlBase: tournament.timeControl.baseTimeMinutes,
        timeControlIncrement: tournament.timeControl.incrementSeconds,
      });
    } catch (error) {
      console.error('Error fetching tournament:', error.response ? error.response.data : error.message);
    }
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    const tournament = {
      id: this.state.id,
      name: this.state.name,
      description: this.state.description,
      startDateTime: this.state.startDateTime,
      endDateTime: this.state.endDateTime,
      registrationStartDate: this.state.registrationStartDate,
      registrationEndDate: this.state.registrationEndDate,
      format: this.state.format,
      status: this.state.status,
      minRating: this.state.minRating,
      maxRating: this.state.maxRating,
      affectsRating: this.state.affectsRating,
      minAge: this.state.minAge,
      maxAge: this.state.maxAge,
      requiredGender: this.state.requiredGender,
      country: this.state.country,
      region: this.state.region,
      city: this.state.city,
      address: this.state.address,
      timeControl: {
        baseTimeMinutes: this.state.timeControlBase,
        incrementSeconds: this.state.timeControlIncrement,
      },
    };

    try {
      await TournamentService.updateTournament(tournament).then((res) => {
        if (res.data) {
          console.log('Tournament updated successfully');
          console.log(res.data);
        }
      });
    } catch (error) {
      console.error('Error updating tournament:', error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  // Handle checkbox change for affectsRating
  handleCheckboxChange = (e) => {
    this.setState({ affectsRating: e.target.checked });
  };

  // Handle change for time control base and increment
  handleTimeControlChange = (e) => {
    this.setState({ [e.target.name]: parseInt(e.target.value, 10) });
  };

  render() {
    const { 
      name, description, startDateTime, endDateTime, 
      registrationStartDate, registrationEndDate, format, 
      status, minRating, maxRating, affectsRating, minAge, 
      maxAge, requiredGender, country, region, city, address, 
      timeControlBase, timeControlIncrement, countries 
    } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="relative w-full max-w-md">

          {/* Update Tournament Form */}
          <form className="p-8 rounded-lg border border-accent shadow-lg bg-secondary" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6">Update Tournament</h2>

            {/* Input fields are similar to CreateTournament component */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Name</label>
              <input
                type="text"
                name="name"
                value={name}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Description</label>
              <textarea
                name="description"
                value={description}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Start Date & Time</label>
              <input
                type="datetime-local"
                name="startDateTime"
                value={startDateTime}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">End Date & Time</label>
              <input
                type="datetime-local"
                name="endDateTime"
                value={endDateTime}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Registration Start Date</label>
              <input
                type="datetime-local"
                name="registrationStartDate"
                value={registrationStartDate}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Registration End Date</label>
              <input
                type="datetime-local"
                name="registrationEndDate"
                value={registrationEndDate}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Format</label>
              <select
                name="format"
                value={format}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select Format</option>
                <option value="ROUND_ROBIN">Round Robin</option>
                <option value="KNOCKOUT">Knockout</option>
                <option value="SWISS">Swiss</option>
              </select>
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Status</label>
              <select
                name="status"
                value={status}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select Status</option>
                <option value="UPCOMING">Upcoming</option>
                <option value="ONGOING">Ongoing</option>
              </select>
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Minimum Rating</label>
              <input
                type="number"
                name="minRating"
                value={minRating}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Maximum Rating</label>
              <input
                type="number"
                name="maxRating"
                value={maxRating}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="inline-flex items-center">
                <input
                  type="checkbox"
                  name="affectsRating"
                  checked={affectsRating}
                  onChange={this.handleCheckboxChange}
                  className="form-checkbox h-5 w-5 text-blue-600"
                />
                <span className="ml-2 text-primary">Affects Rating</span>
              </label>
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Minimum Age</label>
              <input
                type="number"
                name="minAge"
                value={minAge}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Maximum Age</label>
              <input
                type="number"
                name="maxAge"
                value={maxAge}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Required Gender</label>
              <select
                name="requiredGender"
                value={requiredGender}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select Gender</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="ANY">Any</option>
              </select>
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Country</label>
              <select
                name="country"
                value={country}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">Select Country</option>
                {countries.map((country) => (
                  <option key={country.value} value={country.value}>
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
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Address</label>
              <input
                type="text"
                name="address"
                value={address}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Time Control Base (minutes)</label>
              <input
                type="number"
                name="timeControlBase"
                value={timeControlBase}
                onChange={this.handleTimeControlChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Time Control Increment (seconds)</label>
              <input
                type="number"
                name="timeControlIncrement"
                value={timeControlIncrement}
                onChange={this.handleTimeControlChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <button type="submit" className="w-full bg-primary text-white font-bold py-2 rounded hover:bg-blue-700">
              Update Tournament
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default withNavigateandLocation(UpdateTournament);
