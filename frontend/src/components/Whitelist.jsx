import React, { Component } from 'react';
import withNavigateandLocation from './withNavigateandLocation';
import AdminService from '../services/AdminService';

class Whitelist extends Component {
  constructor(props) {
    super(props);
    this.state = {
      playerId: '',
      reason: '',
      
    };
  }

  // Handle form submission
  handleSubmit = async (e) => {
    e.preventDefault();
    const data = {
      playerId: this.state.playerId,
      reason: this.state.reason,
      
    };

    console.log("Whitelist data => " + JSON.stringify(data));

    try {
      await AdminService.whitelistPlayer(data).then((res) => {
        if (res.data) {
          console.log("Player whitelisted successfully");
          // Navigate or display success message here if needed
        }
      });
    } catch (error) {
      console.error('Whitelist error:', error.response ? error.response.data : error.message);
    }
  };

  // Handle input change
  handleInputChange = (e) => {
    const { name, value } = e.target;
    this.setState({ [name]: value });
  };

  render() {
    const { playerId, reason, banDuration } = this.state;

    return (
      <div className="flex justify-center items-center min-h-screen">
        <form className="p-8 rounded-lg border border-accent shadow-lg w-96 bg-secondary">
          <h2 className="text-2xl font-bold text-center mb-6">Whitelist Player</h2>

          <div className="mb-4">
            <label className="block text-primary text-sm font-bold mb-2">Player ID</label>
            <input
              type="number"
              name="playerId"
              value={playerId}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mb-4">
            <label className="block text-primary text-sm font-bold mb-2">Unban Reason</label>
            <input
              type="text"
              name="reason"
              value={reason}
              onChange={this.handleInputChange}
              required
              className="w-full px-3 py-2 border border-accent rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-full font-bold"
            onClick={this.handleSubmit}
          >
            Whitelist Player
          </button>
        </form>
      </div>
    );
  }
}

export default withNavigateandLocation(Whitelist);
