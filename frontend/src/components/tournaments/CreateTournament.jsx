import React, { Component } from 'react';
import TournamentService from '../../services/TournamentService';
import countryList from 'react-select-country-list'; // Use this package for country list

class CreateTournament extends Component {
  constructor(props) {
    super(props);
    this.state = {
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
      maxRounds: '',
      maxPlayers: '',
      timeControlBase: 10, // Default value for base time in minutes
      timeControlIncrement: 5, // Default value for increment in seconds
      countries: countryList().getData(), // Generate country list
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    const { timeControlBase, timeControlIncrement } = this.state;
    const minRatingNumber = parseFloat(this.state.minRating);
    const maxRatingNumber = parseFloat(this.state.maxRating);

    // Validate that minRating is not greater than maxRating
    if (minRatingNumber > maxRatingNumber) {
      alert("Minimum Rating cannot be greater than Maximum Rating -> " + minRatingNumber + ":" + maxRatingNumber);
      
    }

    e.preventDefault();
    const tournament = {
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
        baseTimeMinutes: timeControlBase,
        incrementSeconds: timeControlIncrement,
      },
      maxRounds: this.state.maxRounds,
      maxPlayers: this.state.maxPlayers,
    };

    console.log('Tournament => ' + JSON.stringify(tournament));

    try {
      await TournamentService.createTournament(tournament).then((res) => {
        if (res.data) {
          console.log('Tournament created successfully');
          console.log(res.data);
          alert('Tournament created successfully');
        }
      });
    } catch (error) {
      console.error('Error creating tournament:', error.response ? error.response.data : error.message);
      alert('Failed to create tournament. Please try again. Reason: ' + error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value === "null" ? null : value });
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
      timeControlBase, timeControlIncrement, countries, maxRounds, maxPlayers,
    } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="relative w-full max-w-5xl">
          {/* Create Tournament Form */}
          <form className="p-8 rounded-lg border border-accent shadow-lg bg-secondary grid grid-cols-1 md:grid-cols-2 gap-8" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6 col-span-full">Create Tournament</h2>
    
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
              <label className="block text-primary text-sm font-bold mb-2">Max Rounds</label>
                <input
                  type="number"
                  name="maxRounds"
                  value={maxRounds}
                  onChange={this.handleInputChange}
                  required
                  className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Max Players</label>
              <input
                type="number"
                name="maxPlayers"
                value={maxPlayers}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

    
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Minimum Rating</label>
              <input
                type="number"
                name="minRating"
                value={minRating}
                onChange={this.handleInputChange}
                step="50"
                required
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
                step="50"
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
    
            <div className="mb-4 flex items-center col-span-full">
              <input
                type="checkbox"
                name="affectsRating"
                checked={affectsRating}
                onChange={this.handleCheckboxChange}
                className="mr-2"
              />
              <label className="text-primary text-sm font-bold">Affects Rating</label>
            </div>
    
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Minimum Age</label>
              <input
                type="number"
                name="minAge"
                value={minAge}
                onChange={this.handleInputChange}
                required
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
                required
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
                <option value="null">Any</option>
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHERS">Others</option>
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
              <label className="block text-primary text-sm font-bold mb-2">Address</label>
              <input
                type="text"
                name="address"
                value={address}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Time Control Base</label>
              <input
                type="number"
                name="timeControlBase"
                value={timeControlBase}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Time Control Increment</label>
              <input
                type="number"
                name="timeControlIncrement"
                value={timeControlIncrement}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
    
            <div className="col-span-full flex justify-center">
              <button type="submit" className="btn btn-primary">
                Create Tournament
              </button>
            </div>
          </form>
        </div>
      </div>
    );
    
    
  }
}

export default CreateTournament;
