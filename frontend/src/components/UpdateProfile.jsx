import React, { Component } from 'react';
import ProfileService from '../services/PlayerService';
import countryList from 'react-select-country-list'; // Use for country list
import withNavigateandLocation from './withNavigateandLocation';

class UpdateProfile extends Component {
  constructor(props) {
    super(props);
    this.state = {
      id: '',
      fullName: '',
      bio: '',
      avatarUrl: '',
      country: '',
      region: '',
      city: '',
      isPublic: false,
      countries: countryList().getData(),
    };
  }

  componentDidMount() {
    this.getProfile();
  }

  async getProfile() {
    try {
      const response = await ProfileService.getProfile(); // Fetch profile data
      const profile = response.data;
      console.log('Profile data:', profile);

      this.setState({
        id: profile.playerId,
        fullName: profile.fullName,
        bio: profile.bio,
        avatarUrl: profile.avatarUrl,
        country: profile.country,
        region: profile.region,
        city: profile.city,
        isPublic: profile.isPublic,
      });
    } catch (error) {
      console.error('Error fetching profile:', error.response ? error.response.data : error.message);
    }
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    const { id } = this.state;
    
    const profile = {
      fullName: this.state.fullName,
      bio: this.state.bio,
      avatarUrl: this.state.avatarUrl,
      country: this.state.country,
      region: this.state.region,
      city: this.state.city,
      isPublic: this.state.isPublic,
    };

    console.log('Updating profile with data:', profile);

    try {
      await ProfileService.updateProfile(id, profile).then((res) => {
        if (res.data) {
          console.log('Profile updated successfully');
        }
        this.props.navigate(`/profile/${id}`);
      });
    } catch (error) {
      console.error('Error updating profile:', error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  // Handle checkbox change for profile visibility
  handleCheckboxChange = (e) => {
    this.setState({ isPublic: e.target.checked });
  };

  render() {
    const { fullName, bio, avatarUrl, country, region, city, isPublic, countries } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="relative w-full max-w-md">

          {/* Update Profile Form */}
          <form className="p-8 rounded-lg border border-accent shadow-lg bg-secondary" onSubmit={this.handleSubmit}>
            <h2 className="text-2xl font-bold text-center mb-6">Update Profile</h2>

            {/* Name */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Full Name</label>
              <input
                type="text"
                name="fullName"
                value={fullName}
                onChange={this.handleInputChange}
                required
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* Bio */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Bio</label>
              <textarea
                name="bio"
                value={bio}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* Avatar URL */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Avatar URL</label>
              <input
                type="text"
                name="avatarUrl"
                value={avatarUrl}
                onChange={this.handleInputChange}
                className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* Country */}
            <div className="mb-4">
              <label className="block text-primary text-sm font-bold mb-2">Country</label>
              <select
                name="country"
                value={country}
                onChange={this.handleInputChange}
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

            {/* Region */}
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

            {/* City */}
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

            {/* Profile Visibility */}
            <div className="mb-4">
              <label className="inline-flex items-center">
                <input
                  type="checkbox"
                  name="isPublic"
                  checked={isPublic}
                  onChange={this.handleCheckboxChange}
                  className="form-checkbox h-5 w-5 text-blue-600"
                />
                <span className="ml-2 text-primary">Profile is Public</span>
              </label>
            </div>

            <button
              type="submit"
              className="w-full bg-blue-500 text-white py-2 px-4 rounded-md hover:bg-blue-600 focus:outline-none"
            >
              Update Profile
            </button>
          </form>
        </div>
      </div>
    );
  }
}

export default withNavigateandLocation(UpdateProfile);
